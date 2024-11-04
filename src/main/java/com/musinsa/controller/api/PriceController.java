package com.musinsa.controller.api;

import com.musinsa.model.ApiResponse;
import com.musinsa.model.dto.CategoryPriceResponse;
import com.musinsa.model.dto.CheapestBrandResponse;
import com.musinsa.model.dto.CheapestProductsResponse;
import com.musinsa.model.enums.Category;
import com.musinsa.service.PriceService;
import com.musinsa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prices")
public class PriceController {
    private final ProductService productService;
    private final PriceService priceService;

    @GetMapping
    public ApiResponse<CheapestProductsResponse> getCheapestPrice() {
        CheapestProductsResponse cheapestPriceOfAllCategory = productService.getCheapestPriceOfAllCategory();
        return ApiResponse.success(cheapestPriceOfAllCategory);
    }

    @GetMapping("/cheapest-brand")
    public ApiResponse<CheapestBrandResponse> getCheapestBrand() {
        CheapestBrandResponse cheapestBrandForAllCategories = productService.getCheapestBrandForAllCategories();
        return ApiResponse.success(cheapestBrandForAllCategories);
    }

    @GetMapping("/categories/{category}")
    public ApiResponse<CategoryPriceResponse> getCategoryPrice(@PathVariable Category category) {
        CategoryPriceResponse categoryPriceComparison = priceService.getCategoryPriceComparison(category);
        return ApiResponse.success(categoryPriceComparison);
    }
}
