package com.pictet.adventurebook.common.result;

import java.util.Objects;
import java.util.function.Function;

public sealed interface Result<T> permits Success, Failure {

    boolean isSuccess();

    boolean isFailure();

    T getValue();

    AppError getError();

    <U> Result<U> map(Function<T, U> fn);

    <U> Result<U> flatMap(Function<T, Result<U>> fn);

    <U> U fold(Function<T, U> onSuccess, Function<AppError, U> onFailure);

    T getOrDefault(T defaultValue);

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static Result<Void> success() {
        return new Success<>(null);
    }

    static <X> Result<X> failure(AppError error) {
        Objects.requireNonNull(error, "AppError must not be null");
        return new Failure<>(error);
    }

    static <X> Result<X> failure(ErrorCode code, String message) {
        Objects.requireNonNull(message, "message must not be null");
        AppError error = new AppError(code, message);
        return new Failure<>(error);
    }
}
