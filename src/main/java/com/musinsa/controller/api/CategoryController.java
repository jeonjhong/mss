package com.musinsa.controller.api;

import com.musinsa.model.ApiResponse;
import com.musinsa.model.dto.CategoryResponse;
import com.musinsa.model.entity.CategoryEntity;
import com.musinsa.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getCategories() {
        List<CategoryResponse> list = categoryRepository.findAll().stream().map(CategoryEntity::toResponse).toList();
        return ApiResponse.success(list);
    }
}
