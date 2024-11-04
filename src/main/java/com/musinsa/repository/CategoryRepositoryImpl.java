package com.musinsa.repository;

import com.musinsa.model.entity.CategoryEntity;

public class CategoryRepositoryImpl extends QueryDslBaseRepository<CategoryEntity> implements CategoryRepositoryCustom {
    protected CategoryRepositoryImpl() {
        super(CategoryEntity.class);
    }
}
