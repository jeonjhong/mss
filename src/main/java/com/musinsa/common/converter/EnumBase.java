package com.musinsa.common.converter;

import com.fasterxml.jackson.annotation.JsonValue;

public interface EnumBase {
    @JsonValue
    String getJsonValue();

    String getJsonSerializeValue();
}
