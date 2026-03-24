package com.pictet.adventurebook.common.result;

public enum ErrorCode {

    // Internal errors
    INTERNAL_ERROR(ErrorType.INTERNAL),

    // Validation errors
    VALIDATION_ERROR(ErrorType.VALIDATION),
    INVALID_INPUT(ErrorType.VALIDATION),

    // Not found errors
    NOT_FOUND(ErrorType.NOT_FOUND),

    // Authorization errors
    UNAUTHORIZED(ErrorType.AUTHORIZATION),
    FORBIDDEN(ErrorType.FORBIDDEN),

    // Conflict errors
    CONFLICT(ErrorType.CONFLICT);

    private final ErrorType type;

    ErrorCode(ErrorType type) {
        this.type = type;
    }

    public ErrorType getType() {
        return type;
    }
}
