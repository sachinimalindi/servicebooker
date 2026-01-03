package com.servicebooker.api.common;

public final class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }

    public static NotFoundException of(String resource, String id) {
        return new NotFoundException(resource + " not found: " + id);
    }
}
