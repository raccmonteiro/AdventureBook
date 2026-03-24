package com.pictet.adventurebook.common.result;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void shouldCreateSuccess() {
        Result<String> result = Result.success("test");
        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
        assertEquals("test", result.getValue());
    }

    @Test
    void shouldCreateVoidSuccess() {
        Result<Void> result = Result.success();
        assertTrue(result.isSuccess());
        assertNull(result.getValue());
    }

    @Test
    void shouldCreateFailure() {
        AppError error = new AppError(ErrorCode.NOT_FOUND, "Not found");
        Result<String> result = Result.failure(error);
        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertEquals(error, result.getError());
    }

    @Test
    void shouldCreateFailureWithCodeAndMessage() {
        Result<String> result = Result.failure(ErrorCode.NOT_FOUND, "Not found");
        assertTrue(result.isFailure());
        assertEquals(ErrorCode.NOT_FOUND, result.getError().getCode());
        assertEquals("Not found", result.getError().getMessage());
    }

    @Test
    void shouldThrowExceptionWhenGettingErrorFromSuccess() {
        Result<String> result = Result.success("test");
        assertThrows(UnsupportedOperationException.class, result::getError);
    }

    @Test
    void shouldThrowExceptionWhenGettingValueFromFailure() {
        Result<String> result = Result.failure(ErrorCode.NOT_FOUND, "Not found");
        assertThrows(UnsupportedOperationException.class, result::getValue);
    }

    @Test
    void shouldMapSuccess() {
        Result<Integer> result = Result.success(10).map(i -> i * 2);
        assertTrue(result.isSuccess());
        assertEquals(20, result.getValue());
    }

    @Test
    void shouldNotMapFailure() {
        Result<Integer> result = Result.<Integer>failure(ErrorCode.NOT_FOUND, "Not found")
                .map(i -> i * 2);
        assertTrue(result.isFailure());
        assertEquals(ErrorCode.NOT_FOUND, result.getError().getCode());
    }

    @Test
    void shouldFlatMapSuccess() {
        Result<Integer> result = Result.success(10).flatMap(i -> Result.success(i * 2));
        assertTrue(result.isSuccess());
        assertEquals(20, result.getValue());
    }

    @Test
    void shouldNotFlatMapFailure() {
        Result<Integer> result = Result.<Integer>failure(ErrorCode.NOT_FOUND, "Not found")
                .flatMap(i -> Result.success(i * 2));
        assertTrue(result.isFailure());
        assertEquals(ErrorCode.NOT_FOUND, result.getError().getCode());
    }

    @Test
    void shouldFoldSuccess() {
        String result = Result.success(10).fold(
                i -> "Success: " + i,
                e -> "Failure: " + e.getMessage()
        );
        assertEquals("Success: 10", result);
    }

    @Test
    void shouldFoldFailure() {
        String result = Result.<Integer>failure(ErrorCode.NOT_FOUND, "Error").fold(
                i -> "Success: " + i,
                e -> "Failure: " + e.getMessage()
        );
        assertEquals("Failure: Error", result);
    }

    @Test
    void shouldGetOrDefault() {
        assertEquals(10, Result.success(10).getOrDefault(20));
        assertEquals(20, Result.<Integer>failure(ErrorCode.NOT_FOUND, "Error").getOrDefault(20));
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        Result<String> s1 = Result.success("test");
        Result<String> s2 = Result.success("test");
        Result<String> s3 = Result.success("other");
        Result<String> f1 = Result.failure(ErrorCode.NOT_FOUND, "error");
        Result<String> f2 = Result.failure(ErrorCode.NOT_FOUND, "error");

        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
        assertNotEquals(s1, f1);
        assertEquals(f1, f2);

        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1.hashCode(), s3.hashCode());
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    void shouldTestToString() {
        assertEquals("Success{value=test}", Result.success("test").toString());
        AppError error = new AppError(ErrorCode.NOT_FOUND, "error");
        assertEquals("Failure{error=" + error + "}", Result.failure(error).toString());
    }
}
