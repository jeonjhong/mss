package com.musinsa.repository;

import com.musinsa.model.dto.Product;
import com.musinsa.model.entity.BrandEntity;
import com.musinsa.model.entity.ProductEntity;
import com.musinsa.model.enums.Category;
import com.querydsl.core.types.Projections;

import java.util.List;
import java.util.Optional;

import static com.musinsa.model.entity.QBrandEntity.brandEntity;
import static com.musinsa.model.entity.QCategoryEntity.categoryEntity;
import static com.musinsa.model.entity.QProductEntity.productEntity;


public class ProductRepositoryImpl extends QueryDslBaseRepository<ProductEntity> implements ProductRepositoryCustom {
    protected ProductRepositoryImpl() {
        super(ProductEntity.class);
    }

    @Override
    public List<Product> getAllProducts() {

        return getQueryFactory()
                .select(Projections.constructor(
                        Product.class,
                        productEntity.id,
                        productEntity.price,
                        brandEntity.name,
                        categoryEntity.name
                ))
                .from(productEntity)
                .join(productEntity.brand, brandEntity)
                .join(productEntity.category, categoryEntity)
                .fetch();
    }

    @Override
    public List<BrandEntity> findAllBrands() {

        return getQueryFactory()
                .select(productEntity.brand)
                .distinct()
                .from(productEntity)
                .fetch();
    }

    @Override
    public Optional<ProductEntity> findLowestPriceProductByCategory(Category category) {
        ProductEntity result = getQueryFactory()
                .selectFrom(productEntity)
                .where(productEntity.category.name.eq(category))
                .orderBy(productEntity.price.asc())
                .fetchFirst();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<ProductEntity> findHighestPriceProductByCategory(Category category) {
        ProductEntity result = getQueryFactory()
                .selectFrom(productEntity)
                .where(productEntity.category.name.eq(category))
                .orderBy(productEntity.price.desc())
                .fetchFirst();

        return Optional.ofNullable(result);
    }
}
