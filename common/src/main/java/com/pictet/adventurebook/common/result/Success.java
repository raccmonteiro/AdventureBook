package com.pictet.adventurebook.common.result;

import java.util.Objects;
import java.util.function.Function;

public final class Success<T> implements Result<T> {

    private final T value;

    Success(T value) {
        this.value = value;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public AppError getError() {
        throw new UnsupportedOperationException("Cannot get error from a Success result");
    }

    @Override
    public <U> Result<U> map(Function<T, U> fn) {
        Objects.requireNonNull(fn, "Mapping function must not be null");
        return Result.success(fn.apply(value));
    }

    @Override
    public <U> Result<U> flatMap(Function<T, Result<U>> fn) {
        Objects.requireNonNull(fn, "FlatMap function must not be null");
        Result<U> mapped = fn.apply(value);
        Objects.requireNonNull(mapped, "FlatMap function must not return null");
        return mapped;
    }

    @Override
    public <U> U fold(Function<T, U> onSuccess, Function<AppError, U> onFailure) {
        Objects.requireNonNull(onSuccess, "onSuccess function must not be null");
        Objects.requireNonNull(onFailure, "onFailure function must not be null");
        return onSuccess.apply(value);
    }

    @Override
    public T getOrDefault(T defaultValue) {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Success<?> other)) return false;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Success{value=%s}".formatted(value);
    }
}
