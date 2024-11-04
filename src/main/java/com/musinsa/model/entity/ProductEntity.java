package com.musinsa.model.entity;

import com.musinsa.model.IdGettable;
import com.musinsa.model.dto.CommonIdResponse;
import com.musinsa.model.dto.ProductRequest;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Product")
@Getter
public class ProductEntity extends BaseEntity implements IdGettable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "price", nullable = false)
    private Integer price;

    // ManyToOne 관계 설정: 브랜드와 카테고리 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)  // 외래 키로 brand_id 사용
    private BrandEntity brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)  // 외래 키로 category_id 사용
    private CategoryEntity category;

    public void updateBy(ProductRequest productRequest, BrandEntity brandEntity, CategoryEntity categoryEntity) {
        this.brand = brandEntity;
        this.category = categoryEntity;
        this.price = productRequest.getPrice();
    }

    public CommonIdResponse toResponse() {
        return CommonIdResponse.of(this);
    }
}
