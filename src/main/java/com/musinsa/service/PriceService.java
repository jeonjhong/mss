package com.musinsa.service;

import com.musinsa.exception.ApiException;
import com.musinsa.model.dto.CategoryPriceResponse;
import com.musinsa.model.dto.Product;
import com.musinsa.model.entity.BrandEntity;
import com.musinsa.model.entity.CategoryEntity;
import com.musinsa.model.entity.MinMaxPriceEntity;
import com.musinsa.model.entity.ProductEntity;
import com.musinsa.model.enums.Category;
import com.musinsa.repository.BrandRepository;
import com.musinsa.repository.CategoryRepository;
import com.musinsa.repository.MinMaxPriceRepository;
import com.musinsa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final ProductRepository productRepository;
    private final MinMaxPriceRepository minMaxPriceRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public CategoryPriceResponse getCategoryPriceComparison(Category category) {
        Optional<MinMaxPriceEntity> lowestPriceByCategory = minMaxPriceRepository.findLowestPriceByCategory(category);
        Optional<MinMaxPriceEntity> highestPriceByCategory = minMaxPriceRepository.findHighestPriceByCategory(category);

        Product lowestPriceProduct;
        Product highestPriceProduct;

        if (lowestPriceByCategory.isEmpty()) {
            Optional<ProductEntity> lowestProduct = productRepository.findLowestPriceProductByCategory(category);
            if (lowestProduct.isEmpty()) {
                throw new ApiException("No lowest price found for this category.");
            }
            lowestPriceProduct = Product.of(lowestProduct.get());
        } else {
            lowestPriceProduct = Product.of(lowestPriceByCategory.get());
        }

        if (highestPriceByCategory.isEmpty()) {
            Optional<ProductEntity> highestProduct = productRepository.findHighestPriceProductByCategory(category);
            if (highestProduct.isEmpty()) {
                throw new ApiException("No highest price found for this category.");
            }
            highestPriceProduct = Product.of(highestProduct.get());
        } else {
            highestPriceProduct = Product.of(highestPriceByCategory.get());
        }


        // 응답 객체 생성
        return CategoryPriceResponse.of(
                category,
                lowestPriceProduct,
                highestPriceProduct
        );
    }

    @Transactional(readOnly = true)
    public List<MinMaxPriceEntity> getMinMaxPrices() {
        List<BrandEntity> brands = brandRepository.findAll();
        List<CategoryEntity> categories = categoryRepository.findAll();
        List<MinMaxPriceEntity> minMaxPriceEntities = new ArrayList<>();

        for (BrandEntity brandEntity : brands) {
            for (CategoryEntity categoryEntity : categories) {
                // 해당 브랜드/카테고리에 대한 최소/최대 가격 조회
                Optional<ProductEntity> minPriceProductOpt =
                        productRepository.findTopByBrandAndCategoryOrderByPriceAsc(brandEntity, categoryEntity);
                Optional<ProductEntity> maxPriceProductOpt =
                        productRepository.findTopByBrandAndCategoryOrderByPriceDesc(brandEntity, categoryEntity);

                if (minPriceProductOpt.isPresent() && maxPriceProductOpt.isPresent()) {
                    ProductEntity minPriceProduct = minPriceProductOpt.get();
                    ProductEntity maxPriceProduct = maxPriceProductOpt.get();

                    // 기존 데이터가 있는지 확인하고 없으면 새로 생성, 있으면 업데이트
                    MinMaxPriceEntity minMaxPrice = minMaxPriceRepository.findByBrandAndCategory(brandEntity, categoryEntity)
                            .orElse(new MinMaxPriceEntity());

                    // 브랜드 및 카테고리 설정
                    minMaxPrice.setBrand(brandEntity);
                    minMaxPrice.setCategory(categoryEntity);

                    // 최소/최대 가격 및 해당 상품 ID 설정
                    minMaxPrice.setMinPrice(minPriceProduct.getPrice());
                    minMaxPrice.setMaxPrice(maxPriceProduct.getPrice());
                    minMaxPrice.setMinPriceProductId(minPriceProduct.getId());  // 최소가 상품 ID 저장
                    minMaxPrice.setMaxPriceProductId(maxPriceProduct.getId());  // 최대가 상품 ID 저장

                    // 결과 리스트에 추가 (저장하지 않고 반환만 할 경우)
                    minMaxPriceEntities.add(minMaxPrice);
                }
            }
        }

        return minMaxPriceEntities;
    }
}
