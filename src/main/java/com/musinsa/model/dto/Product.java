package com.musinsa.model.dto;

import com.musinsa.model.entity.MinMaxPriceEntity;
import com.musinsa.model.entity.ProductEntity;
import com.musinsa.model.enums.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Product {
    private final long productId;
    private final int price;
    private final String brand;
    private final Category category;

    public static Product of(MinMaxPriceEntity minMaxPriceEntity) {
        return new Product(minMaxPriceEntity.getMinPriceProductId(), minMaxPriceEntity.getMinPrice(), minMaxPriceEntity.getBrand().getName(), minMaxPriceEntity.getCategory().getName());
    }

    public static Product of(ProductEntity productEntity) {
        return new Product(productEntity.getId(), productEntity.getPrice(), productEntity.getBrand().getName(), productEntity.getCategory().getName());
    }
}
