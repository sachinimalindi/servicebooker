package com.servicebooker.api.dto.common;


public record ApiResponse<T>(T data, PageMeta page) {
    public static <T> ApiResponse<T> of(T data) { return new ApiResponse<>(data, null); }
    public static <T> ApiResponse<T> of(T data, PageMeta page) { return new ApiResponse<>(data, page); }
}
