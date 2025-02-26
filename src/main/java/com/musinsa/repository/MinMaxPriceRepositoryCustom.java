package com.musinsa.repository;

import com.musinsa.model.entity.BrandEntity;
import com.musinsa.model.entity.MinMaxPriceEntity;
import com.musinsa.model.enums.Category;

import java.util.List;
import java.util.Optional;

public interface MinMaxPriceRepositoryCustom {
    Optional<MinMaxPriceEntity> findLowestPriceByCategory(Category category);

    Optional<MinMaxPriceEntity> findHighestPriceByCategory(Category category);

    List<MinMaxPriceEntity> findAllByBrand(BrandEntity brandEntity);
}
