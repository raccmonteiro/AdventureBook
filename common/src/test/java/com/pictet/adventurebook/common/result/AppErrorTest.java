package com.pictet.adventurebook.common.result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppErrorTest {

    @Test
    void shouldCreateAppError() {
        AppError error = new AppError(ErrorCode.VALIDATION_ERROR, "Invalid data");
        assertEquals(ErrorCode.VALIDATION_ERROR, error.getCode());
        assertEquals("Invalid data", error.getMessage());
        assertEquals(ErrorType.VALIDATION, error.getType());
    }

    @Test
    void shouldThrowExceptionWhenCodeIsNull() {
        assertThrows(NullPointerException.class, () -> new AppError(null, "Message"));
    }

    @Test
    void shouldThrowExceptionWhenMessageIsNull() {
        assertThrows(NullPointerException.class, () -> new AppError(ErrorCode.INTERNAL_ERROR, null));
    }

    @Test
    void shouldThrowExceptionWhenMessageIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new AppError(ErrorCode.INTERNAL_ERROR, ""));
        assertThrows(IllegalArgumentException.class, () -> new AppError(ErrorCode.INTERNAL_ERROR, "  "));
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        AppError e1 = new AppError(ErrorCode.NOT_FOUND, "Not found");
        AppError e2 = new AppError(ErrorCode.NOT_FOUND, "Not found");
        AppError e3 = new AppError(ErrorCode.NOT_FOUND, "Missing");
        AppError e4 = new AppError(ErrorCode.INTERNAL_ERROR, "Not found");

        assertEquals(e1, e2);
        assertNotEquals(e1, e3);
        assertNotEquals(e1, e4);
        assertNotEquals(e1, null);

        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1.hashCode(), e3.hashCode());
    }

    @Test
    void shouldTestToString() {
        AppError error = new AppError(ErrorCode.NOT_FOUND, "Not found");
        assertEquals("AppError{code=NOT_FOUND, message='Not found'}", error.toString());
    }
}
