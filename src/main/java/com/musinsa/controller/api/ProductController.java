package com.musinsa.controller.api;

import com.musinsa.model.ApiResponse;
import com.musinsa.model.dto.CommonIdResponse;
import com.musinsa.model.dto.ProductRequest;
import com.musinsa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ApiResponse<CommonIdResponse> saveProduct(@RequestBody ProductRequest productRequest) {
        CommonIdResponse commonIdResponse = productService.saveOrUpdateProduct(productRequest);
        return ApiResponse.success(commonIdResponse);
    }

    @PutMapping("/{id}")
    public ApiResponse<CommonIdResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        productRequest.setId(id);
        CommonIdResponse commonIdResponse = productService.saveOrUpdateProduct(productRequest);
        return ApiResponse.success(commonIdResponse);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<CommonIdResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.success(CommonIdResponse.of(() -> id));
    }
}
