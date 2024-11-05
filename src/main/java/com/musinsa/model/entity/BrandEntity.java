package com.musinsa.model.entity;

import com.musinsa.model.IdGettable;
import com.musinsa.model.dto.CommonIdResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Brand")
@Getter
@Setter
public class BrandEntity extends BaseEntity implements IdGettable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;
    @Column(name = "brand_name")
    private String name;

    public CommonIdResponse toResponse() {
        return CommonIdResponse.of(this);
    }
}
