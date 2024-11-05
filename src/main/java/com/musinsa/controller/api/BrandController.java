package com.musinsa.controller.api;

import com.musinsa.model.ApiResponse;
import com.musinsa.model.dto.BrandRequest;
import com.musinsa.model.dto.CommonIdResponse;
import com.musinsa.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brands")
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    public ApiResponse<CommonIdResponse> saveOrUpdateBrand(@RequestBody BrandRequest brandRequest) {
        CommonIdResponse commonIdResponse = brandService.saveOrUpdateBrand(brandRequest);
        return ApiResponse.success(commonIdResponse);
    }

    @PutMapping("/{id}")
    public ApiResponse<CommonIdResponse> updateBrand(@PathVariable Long id, @RequestBody BrandRequest brandRequest) {
        brandRequest.setId(id);
        CommonIdResponse commonIdResponse = brandService.saveOrUpdateBrand(brandRequest);
        return ApiResponse.success(commonIdResponse);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<CommonIdResponse> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ApiResponse.success(CommonIdResponse.of(() -> id));
    }
}
