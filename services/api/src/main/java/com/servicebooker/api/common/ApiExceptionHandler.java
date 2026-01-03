package com.servicebooker.api.common;

import com.servicebooker.api.dto.common.ErrorItem;
import com.servicebooker.api.dto.common.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        HttpStatus status = switch (ex.code()) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case VALIDATION, BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        List<ErrorItem> errors = List.of();
        if (ex instanceof ValidationException vex) {
            errors = vex.errors().stream()
                    .map(e -> new ErrorItem(e.field(), e.message()))
                    .toList();
        }

        return ResponseEntity.status(status)
                .body(new ErrorResponse(ex.code().name(), ex.getMessage(), errors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toErrorItem)
                .toList();

        var body = new ErrorResponse(ErrorCode.VALIDATION.name(), "Validation failed", errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorItem> errors = ex.getConstraintViolations().stream()
                .map(v -> new ErrorItem(
                        v.getPropertyPath() == null ? "" : v.getPropertyPath().toString(),
                        v.getMessage()
                ))
                .toList();

        var body = new ErrorResponse(ErrorCode.VALIDATION.name(), "Validation failed", errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadJson(HttpMessageNotReadableException ex) {
        var body = new ErrorResponse(ErrorCode.BAD_REQUEST.name(), "Malformed JSON request", List.of());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandled(Exception ex) {
        // Do NOT leak exception details to clients
        var body = new ErrorResponse(ErrorCode.INTERNAL.name(), "Unexpected server error", List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private ErrorItem toErrorItem(FieldError fe) {
        return new ErrorItem(fe.getField(), fe.getDefaultMessage());
    }
}