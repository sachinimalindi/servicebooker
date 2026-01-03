package com.servicebooker.api.common;

import java.util.List;

public final class ValidationException extends AppException {
    private final List<FieldError> errors;

    public ValidationException(String message, List<FieldError> errors) {
        super(ErrorCode.VALIDATION, message);
        this.errors = errors == null ? List.of() : List.copyOf(errors);
    }

    public List<FieldError> errors() { return errors; }

    public record FieldError(String field, String message) {}
}
