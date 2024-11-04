package com.musinsa.model.dto;

import com.musinsa.exception.ApiException;
import lombok.Getter;

import java.util.Objects;

@Getter
public class BrandRequest {
    private final Long id;
    private final String name;

    public BrandRequest(Long id, String name) {
        this.id = id;
        if (Objects.isNull(name)) {
            throw new ApiException("brand name cannot be null");
        }
        this.name = name;
    }
}
