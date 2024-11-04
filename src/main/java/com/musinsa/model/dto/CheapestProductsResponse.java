package com.musinsa.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CheapestProductsResponse {
    private final List<Product> products;
    private final int totalPrice;

    private CheapestProductsResponse(List<Product> products) {
        this.products = products;
        this.totalPrice = products.stream().mapToInt(Product::getPrice).sum();
    }

    public static CheapestProductsResponse of(List<Product> products) {
        return new CheapestProductsResponse(products);
    }
}
