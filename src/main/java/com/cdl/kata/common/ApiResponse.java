package com.cdl.kata.common;

public class ApiResponse<T> {
    private T data;
    private String error;

    public static <T> ApiResponse<T> success(T dto) {
        ApiResponse<T> r = new ApiResponse<>();
        r.data = dto;
        return r;
    }

    public static <T> ApiResponse<T> error(String msg) {
        ApiResponse<T> r = new ApiResponse<>();
        r.error = msg;
        return r;
    }

    public boolean isSuccess() {
        return data != null;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}