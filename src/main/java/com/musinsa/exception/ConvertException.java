package com.musinsa.exception;

public class ConvertException extends RuntimeException {
    public ConvertException() {
        super("failed to convert json to enum");
    }
}
