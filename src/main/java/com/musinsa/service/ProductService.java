package com.musinsa.service;

import com.musinsa.exception.ApiException;
import com.musinsa.model.dto.*;
import com.musinsa.model.entity.BrandEntity;
import com.musinsa.model.entity.CategoryEntity;
import com.musinsa.model.entity.MinMaxPriceEntity;
import com.musinsa.model.entity.ProductEntity;
import com.musinsa.repository.BrandRepository;
import com.musinsa.repository.CategoryRepository;
import com.musinsa.repository.MinMaxPriceRepository;
import com.musinsa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final MinMaxPriceRepository minMaxPriceRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Transactional(readOnly = true)
    public CheapestProductsResponse getCheapestPriceOfAllCategory() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        List<MinMaxPriceEntity> minMaxPrices = minMaxPriceRepository.findByCategoryIn(categories);

        // MinMaxPrice 테이블에서 조회한 최소 가격 상품 리스트 생성
        List<Product> minPriceList = minMaxPrices.stream()
                .map(MinMaxPriceEntity::toMinPriceProduct)
                .toList();

        // MinMaxPrice 테이블에 데이터가 없으면 전체 상품에서 계산
        if (minPriceList.isEmpty()) {
            minPriceList = productRepository.getAllProducts();
        }

        // 카테고리별로 그룹화하여 최저 가격 상품 찾기
        List<Product> productList = minPriceList.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.minBy(Comparator.comparingInt(Product::getPrice))))
                .values().stream()
                .flatMap(Optional::stream)  // Optional이 있는 경우만 처리
                .sorted(Comparator.comparing(Product::getBrand))
                .toList();// 최종적으로 카테고리별 최저가 상품 리스트 반환
        return CheapestProductsResponse.of(productList);
    }

    @Transactional(readOnly = true)
    public CheapestBrandResponse getCheapestBrandForAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        List<BrandEntity> brands = productRepository.findAllBrands();

        return brands.stream()
                .map(brand -> calculateBrandTotalPrice(categories, brand))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .min(Comparator.comparingInt(CheapestBrandResponse::getTotalPrice))
                .orElseThrow(() -> new ApiException("No brand found that provides products for all categories"));
    }

    protected Optional<CheapestBrandResponse> calculateBrandTotalPrice(List<CategoryEntity> categories, BrandEntity brand) {
        List<MinMaxPriceEntity> minMaxPrices = minMaxPriceRepository.findByBrand(brand);

        if (minMaxPrices.isEmpty()) {
            minMaxPrices = findLowestPricesFromAllProducts(brand);
        }

        Map<CategoryEntity, MinMaxPriceEntity> priceMap = minMaxPrices.stream()
                .collect(Collectors.toMap(MinMaxPriceEntity::getCategory, Function.identity()));

        boolean hasAllCategories = categories.stream().allMatch(priceMap::containsKey);

        if (!hasAllCategories) {
            return Optional.empty();
        }

        List<Product> categoryPrices = new ArrayList<>();

        for (CategoryEntity category : categories) {
            MinMaxPriceEntity minMaxPrice = priceMap.get(category);
            Product product = Product.of(minMaxPrice);
            categoryPrices.add(product);
        }

        return Optional.of(CheapestBrandResponse.of(brand.getName(), categoryPrices));
    }

    private List<MinMaxPriceEntity> findLowestPricesFromAllProducts(BrandEntity brand) {
        List<ProductEntity> allProducts = productRepository.findAllByBrand(brand);

        return allProducts.stream()
                .collect(Collectors.toMap(
                        ProductEntity::getCategory,
                        Function.identity(),
                        (existingProduct, newProduct) -> newProduct.getPrice() < existingProduct.getPrice() ? newProduct : existingProduct
                ))
                .entrySet().stream()
                .map(entry -> createMinMaxPriceFromProduct(entry.getValue(), brand, entry.getKey()))
                .toList();
    }

    private MinMaxPriceEntity createMinMaxPriceFromProduct(ProductEntity product, BrandEntity brand, CategoryEntity category) {
        MinMaxPriceEntity minMaxPrice = new MinMaxPriceEntity();
        minMaxPrice.setBrand(brand);
        minMaxPrice.setCategory(category);
        minMaxPrice.setMinPrice(product.getPrice());
        minMaxPrice.setMinPriceProductId(product.getId());

        return minMaxPrice;
    }

    @Transactional
    public CommonIdResponse saveOrUpdateProduct(ProductRequest productRequest) {
        BrandEntity brand = brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new ApiException("Brand not found"));
        CategoryEntity category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ApiException("Category not found"));

        ProductEntity productEntity;
        if (Objects.nonNull(productRequest.getId())) {
            productEntity = productRepository.findById(productRequest.getId())
                    .orElseThrow(() -> new ApiException("Product not found"));
        } else {
            productEntity = new ProductEntity();
        }

        productEntity.updateBy(productRequest, brand, category);

        ProductEntity savedProduct = productRepository.save(productEntity);

        updateMinMaxPrice(savedProduct);

        return savedProduct.toResponse();
    }


    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ApiException("Product not found");
        }
        productRepository.deleteById(productId);
    }

    private void updateMinMaxPrice(ProductEntity product) {
        MinMaxPriceEntity minMaxPrice = minMaxPriceRepository.findByBrandAndCategory(product.getBrand(), product.getCategory())
                .orElse(MinMaxPriceEntity.of(product));

        if (minMaxPrice.getMinPrice() == 0 || product.getPrice() < minMaxPrice.getMinPrice()) {
            minMaxPrice.setMinPrice(product.getPrice());
            minMaxPrice.setMinPriceProductId(product.getId());
        }

        if (product.getPrice() > minMaxPrice.getMaxPrice()) {
            minMaxPrice.setMaxPrice(product.getPrice());
            minMaxPrice.setMaxPriceProductId(product.getId());
        }

        minMaxPriceRepository.save(minMaxPrice);
    }

}
