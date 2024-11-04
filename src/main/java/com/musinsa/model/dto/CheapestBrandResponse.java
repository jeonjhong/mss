package com.musinsa.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CheapestBrandResponse {
    private final String brand;
    private final List<Product> products;
    private final int totalPrice;

    public CheapestBrandResponse(String brand, List<Product> products) {
        this.brand = brand;
        this.products = products;
        this.totalPrice = products.stream().mapToInt(Product::getPrice).sum();
    }

    public static CheapestBrandResponse of(String brand, List<Product> products) {
        return new CheapestBrandResponse(brand, products);
    }
}
