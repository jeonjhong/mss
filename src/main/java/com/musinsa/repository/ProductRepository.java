package com.musinsa.repository;

import com.musinsa.model.entity.BrandEntity;
import com.musinsa.model.entity.CategoryEntity;
import com.musinsa.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {
    // 브랜드와 카테고리에 따른 최소 가격을 가진 상품 조회 (최저가 상품)
    Optional<ProductEntity> findTopByBrandAndCategoryOrderByPriceAsc(BrandEntity brand, CategoryEntity category);

    // 브랜드와 카테고리에 따른 최대 가격을 가진 상품 조회 (최대가 상품)
    Optional<ProductEntity> findTopByBrandAndCategoryOrderByPriceDesc(BrandEntity brand, CategoryEntity category);

}
