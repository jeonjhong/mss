package com.musinsa.model.dto;

import com.musinsa.model.entity.BrandEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BrandResponse {
    private Long id;

    public static BrandResponse of(BrandEntity brandEntity) {
        return new BrandResponse(brandEntity.getId());
    }
}
