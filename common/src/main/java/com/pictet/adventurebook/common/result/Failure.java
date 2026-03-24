package com.pictet.adventurebook.common.result;

import java.util.Objects;
import java.util.function.Function;

public final class Failure<T> implements Result<T> {

    private final AppError error;

    Failure(AppError error) {
        this.error = Objects.requireNonNull(error, "AppError must not be null");
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public T getValue() {
        throw new UnsupportedOperationException("Cannot get value from a Failure result: " + error);
    }

    @Override
    public AppError getError() {
        return error;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Result<U> map(Function<T, U> fn) {
        Objects.requireNonNull(fn, "Mapping function must not be null");
        return (Result<U>) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Result<U> flatMap(Function<T, Result<U>> fn) {
        Objects.requireNonNull(fn, "FlatMap function must not be null");
        return (Result<U>) this;
    }

    @Override
    public <U> U fold(Function<T, U> onSuccess, Function<AppError, U> onFailure) {
        Objects.requireNonNull(onSuccess, "onSuccess function must not be null");
        Objects.requireNonNull(onFailure, "onFailure function must not be null");
        return onFailure.apply(error);
    }

    @Override
    public T getOrDefault(T defaultValue) {
        return defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Failure<?> other)) return false;
        return error.equals(other.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error);
    }

    @Override
    public String toString() {
        return "Failure{error=%s}".formatted(error);
    }
}
