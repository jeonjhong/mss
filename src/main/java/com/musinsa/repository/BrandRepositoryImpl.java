package com.musinsa.repository;

import com.musinsa.model.entity.BrandEntity;

public class BrandRepositoryImpl extends QueryDslBaseRepository<BrandEntity> implements BrandRepositoryCustom {
    protected BrandRepositoryImpl() {
        super(BrandEntity.class);
    }
}
