package com.musinsa.common.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/**
 * 문자열 데이터를 적절한 CommonTypeAndStatus 타입의 Enum으로 변환해주는 컨버터
 *
 * @author dami.son
 */
@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum<? extends EnumBase>> {

    @Override
    public <T extends Enum<? extends EnumBase>> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter(targetType);
    }

    private static class StringToEnumConverter<E extends Enum<E> & EnumBase> implements Converter<String, E> {
        private Class<E> enumType;

        StringToEnumConverter(Class<E> enumType) {
            this.enumType = enumType;
        }

        @Override
        public E convert(String source) {
            return StringToEnumConvertUtil.fromJsonValue(enumType, source);
        }
    }
}
