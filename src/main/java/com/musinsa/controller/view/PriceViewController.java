package com.musinsa.controller.view;

import com.musinsa.model.dto.CheapestBrandResponse;
import com.musinsa.model.dto.CheapestProductsResponse;
import com.musinsa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PriceViewController {

    private final ProductService productService;

    @GetMapping("/prices")
    public String getCategoryPrices(Model model) {
        CheapestProductsResponse cheapestPriceOfAllCategory = productService.getCheapestPriceOfAllCategory();
        model.addAttribute("products", cheapestPriceOfAllCategory.getProducts());
        model.addAttribute("totalPrice", cheapestPriceOfAllCategory.getTotalPrice());
        return "prices";
    }

    @GetMapping("/prices-by-one-brand")
    public String getCategoryPricesByOneBrand(Model model) {
        CheapestBrandResponse cheapestBrandForAllCategories = productService.getCheapestBrandForAllCategories();
        model.addAttribute("products", cheapestBrandForAllCategories.getProducts());
        model.addAttribute("totalPrice", cheapestBrandForAllCategories.getTotalPrice());
        return "prices";
    }
}
