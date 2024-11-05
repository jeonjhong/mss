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
        // 주어진 브랜드에 해당하는 모든 MinMaxPriceEntity를 조회
        List<MinMaxPriceEntity> minMaxPrices = minMaxPriceRepository.findAllByBrand(brand);

        // 가격 정보가 없을 경우, 모든 제품에서 최저가를 찾는 메서드 호출
        if (minMaxPrices.isEmpty()) {
            minMaxPrices = findLowestPricesFromAllProducts(brand);
        }

        // MinMaxPriceEntity를 카테고리별로 Map으로 수집 (중복된 카테고리가 있을 경우, 더 낮은 가격을 선택)
        Map<CategoryEntity, MinMaxPriceEntity> priceMap = minMaxPrices.stream()
                .collect(Collectors.toMap(
                        MinMaxPriceEntity::getCategory,  // 카테고리를 키로 사용
                        Function.identity(),             // MinMaxPriceEntity를 값으로 사용
                        (existing, replacement) -> existing.getMinPrice() < replacement.getMinPrice() ? existing : replacement // 중복된 카테고리일 경우 더 낮은 가격을 가진 엔티티 선택
                ));

        // 모든 카테고리가 priceMap에 존재하는지 확인
        boolean hasAllCategories = categories.stream().allMatch(priceMap::containsKey);

        // 카테고리 중 하나라도 누락되었으면 빈 Optional 반환
        if (!hasAllCategories) {
            return Optional.empty();
        }

        // 각 카테고리에 해당하는 Product 객체 리스트 생성
        List<Product> categoryPrices = categories.stream()
                .map(category -> Product.of(priceMap.get(category)))  // MinMaxPriceEntity를 Product로 변환
                .toList();

        // 브랜드 이름과 카테고리별 제품 리스트를 포함한 응답 반환
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
