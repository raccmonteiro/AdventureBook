package com.pictet.adventurebook.common.result;

import java.util.Objects;

import lombok.Getter;

@Getter
public final class AppError {

    private final ErrorCode code;
    private final String message;

    public AppError(ErrorCode code, String message) {
        this.code = Objects.requireNonNull(code, "ErrorCode must not be null");
        this.message = Objects.requireNonNull(message, "Message must not be null");
        if (message.isBlank()) {
            throw new IllegalArgumentException("Message must not be blank");
        }
    }

    public ErrorType getType() {
        return code.getType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppError other)) return false;
        return code == other.code && message.equals(other.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }

    @Override
    public String toString() {
        return "AppError{code=%s, message='%s'}".formatted(code, message);
    }
}
