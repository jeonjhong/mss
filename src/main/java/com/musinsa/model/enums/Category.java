package com.musinsa.model.enums;

import com.musinsa.common.converter.EnumBase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category implements EnumBase {
    TOP("상의", "top"),
    OUTER("아우터", "outer"),
    PANTS("바지", "pants"),
    SNEAKERS("스니커즈", "sneakers"),
    BAG("가방", "bag"),
    HAT("모자", "hat"),
    SOCKS("양말", "socks"),
    ACCESSORY("액세서리", "accessory"),
    ;
    private final String jsonValue;
    private final String jsonSerializeValue;
}
