package com.musinsa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @Setter
    private Long id;
    private String name;
    private int price;
    private Long brandId;
    private Long categoryId;
}
