package com.musinsa.model.dto;

import com.musinsa.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BrandRequest {
    private String name;
    @Setter
    private Long id;

    public BrandRequest(Long id, String name) {
        this.id = id;
        if (Objects.isNull(name)) {
            throw new ApiException("brand name cannot be null");
        }
        this.name = name;
    }
}
