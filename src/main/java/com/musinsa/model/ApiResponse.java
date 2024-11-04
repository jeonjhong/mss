package com.musinsa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.musinsa.exception.ApiException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean isSuccessful;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "", data);
    }

    public static <T> ApiResponse<T> fail(ApiException apiException) {
        return new ApiResponse<>(false, apiException.getMessage(), null);
    }
}
