package com.musinsa.model.dto;

import com.musinsa.model.enums.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryPriceResponse {
    private final Category category;
    private final Product lowestPrice;
    private final Product highestPrice;

    public static CategoryPriceResponse of(Category category, Product lowestPrice, Product highestPrice) {
        return new CategoryPriceResponse(category, lowestPrice, highestPrice);
    }
}
