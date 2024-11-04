package com.musinsa.model.entity;

import com.musinsa.model.IdGettable;
import com.musinsa.model.dto.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "MinMaxPrice")
@Getter
@Setter
public class MinMaxPriceEntity extends BaseEntity implements IdGettable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(name = "min_price", nullable = false)
    private int minPrice;

    @Column(name = "max_price", nullable = false)
    private int maxPrice;

    // 최소 가격을 가진 상품의 ID
    @Column(name = "min_price_product_id")
    private Long minPriceProductId;

    // 최대 가격을 가진 상품의 ID
    @Column(name = "max_price_product_id")
    private Long maxPriceProductId;

    public static MinMaxPriceEntity of(ProductEntity productEntity) {
        MinMaxPriceEntity minMaxPriceEntity = new MinMaxPriceEntity();
        minMaxPriceEntity.setBrand(productEntity.getBrand());
        minMaxPriceEntity.setCategory(productEntity.getCategory());
        return minMaxPriceEntity;
    }

    public Product toMinPriceProduct() {
        return new Product(1L, this.minPrice, this.brand.getName(), this.category.getName());
    }

}
