package com.musinsa.repository;

import com.musinsa.model.dto.Product;
import com.musinsa.model.entity.BrandEntity;
import com.musinsa.model.entity.ProductEntity;
import com.musinsa.model.enums.Category;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryCustom {
    List<Product> getAllProducts();

    List<BrandEntity> findAllBrands();

    Optional<ProductEntity> findLowestPriceProductByCategory(Category category);

    Optional<ProductEntity> findHighestPriceProductByCategory(Category category);
}
