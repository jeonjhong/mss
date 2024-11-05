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

import java.util.*;

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

        // Use the findByBrandsAndCategories method to fetch all products based on brands and categories
        List<ProductEntity> products = productRepository.findByBrandsAndCategories(brands, categories);

        Map<String, MinMaxPriceEntity> minMaxPriceMap = new HashMap<>();

        for (ProductEntity product : products) {
            String key = product.getBrand().getId() + "-" + product.getCategory().getId();

            // Retrieve or create a new MinMaxPriceEntity for this brand/category combination
            MinMaxPriceEntity minMaxPrice = minMaxPriceMap.computeIfAbsent(key, k -> MinMaxPriceEntity.of(product));

            // Update min/max prices and product IDs
            if (minMaxPrice.getMinPrice() == 0 || product.getPrice().compareTo(minMaxPrice.getMinPrice()) < 0) {
                minMaxPrice.setMinPrice(product.getPrice());
                minMaxPrice.setMinPriceProductId(product.getId());
            }

            if (minMaxPrice.getMaxPrice() == 0 || product.getPrice().compareTo(minMaxPrice.getMaxPrice()) > 0) {
                minMaxPrice.setMaxPrice(product.getPrice());
                minMaxPrice.setMaxPriceProductId(product.getId());
            }
        }

        return new ArrayList<>(minMaxPriceMap.values());
    }
}
