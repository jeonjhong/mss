package com.musinsa.common.converter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.musinsa.exception.ConvertException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.EnumSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringToEnumConvertUtil {
    @JsonCreator
    @Nullable
    public static <T extends Enum<T> & EnumBase> T fromJsonValue(Class<T> enumClass, @Nullable String jsonValue) {
        if (jsonValue == null) {
            return null;
        }

        return EnumSet.allOf(enumClass)
                .stream()
                .filter(value -> value.getJsonSerializeValue().equalsIgnoreCase(jsonValue))
                .findAny()
                .orElseThrow(ConvertException::new);
    }
}
