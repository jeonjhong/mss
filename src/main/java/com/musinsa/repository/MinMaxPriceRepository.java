package com.musinsa.repository;

import com.musinsa.model.entity.BrandEntity;
import com.musinsa.model.entity.CategoryEntity;
import com.musinsa.model.entity.MinMaxPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MinMaxPriceRepository extends JpaRepository<MinMaxPriceEntity, Long>, MinMaxPriceRepositoryCustom {
    Optional<MinMaxPriceEntity> findByBrandAndCategory(BrandEntity brandEntity, CategoryEntity categoryEntity);

    List<MinMaxPriceEntity> findByCategoryIn(List<CategoryEntity> categories);

    Optional<MinMaxPriceEntity> findByCategory(CategoryEntity categoryEntity);

}
