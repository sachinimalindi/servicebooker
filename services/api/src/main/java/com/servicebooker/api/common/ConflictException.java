package com.servicebooker.api.common;

public final class ConflictException extends AppException {
    public ConflictException(String message) {
        super(ErrorCode.CONFLICT, message);
    }
}