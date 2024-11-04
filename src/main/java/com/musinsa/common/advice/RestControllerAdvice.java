package com.musinsa.common.advice;

import com.musinsa.exception.ApiException;
import com.musinsa.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class RestControllerAdvice {
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public <T> ApiResponse<T> handleApiException(ApiException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.fail(e);
    }
}
