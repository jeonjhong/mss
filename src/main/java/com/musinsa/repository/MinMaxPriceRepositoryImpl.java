package com.musinsa.repository;

import com.musinsa.model.entity.BrandEntity;
import com.musinsa.model.entity.MinMaxPriceEntity;
import com.musinsa.model.entity.QBrandEntity;
import com.musinsa.model.entity.QCategoryEntity;
import com.musinsa.model.enums.Category;

import java.util.List;
import java.util.Optional;

import static com.musinsa.model.entity.QMinMaxPriceEntity.minMaxPriceEntity;

public class MinMaxPriceRepositoryImpl extends QueryDslBaseRepository<MinMaxPriceEntity> implements MinMaxPriceRepositoryCustom {
    protected MinMaxPriceRepositoryImpl() {
        super(MinMaxPriceEntity.class);
    }

    @Override
    public Optional<MinMaxPriceEntity> findLowestPriceByCategory(Category category) {
        return Optional.ofNullable(
                getQueryFactory().selectFrom(minMaxPriceEntity)
                        .where(minMaxPriceEntity.category.name.eq(category))
                        .orderBy(minMaxPriceEntity.minPrice.asc())
                        .fetchFirst()
        );
    }


    @Override
    public Optional<MinMaxPriceEntity> findHighestPriceByCategory(Category category) {
        return Optional.ofNullable(
                getQueryFactory().selectFrom(minMaxPriceEntity)
                        .where(minMaxPriceEntity.category.name.eq(category))
                        .orderBy(minMaxPriceEntity.maxPrice.desc())
                        .fetchFirst()
        );
    }

    @Override
    public List<MinMaxPriceEntity> findAllByBrand(BrandEntity brandEntity) {
        return getQueryFactory()
                .selectFrom(minMaxPriceEntity)
                .join(minMaxPriceEntity.brand, QBrandEntity.brandEntity).fetchJoin()
                .join(minMaxPriceEntity.category, QCategoryEntity.categoryEntity).fetchJoin()
                .distinct()
                .where(minMaxPriceEntity.brand.eq(brandEntity))
                .fetch();
    }
}
