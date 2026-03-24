package com.pictet.adventurebook.common.domain;


import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * A simple wrapper class for creating DDD-style Value Objects.
 *
 * Value objects are useful for providing type-safety compared to primitives
 * e.g. preventing you at compile-time from putting a product code into a first name field just because they're both strings.
 *
 * Validation logic can be added by extending the normalize and validate methods.
 * These methods are called from the constructor, meaning that the rest of your code can trust that if an instance of your value object exists, it must be valid.
 *
 * Note: Subclasses should implement a public static valueOf(T value) factory method.
 * Unfortunately, this cannot be enforced at compile time due to Java's static method limitations.
 *
 * @see <a href="https://softwareengineering.stackexchange.com/questions/125817/what-is-a-value-object-in-domain-driven-design">More info</a>
 */
public abstract class Value<T> {

    private final T value;

    protected Value(@Nonnull final T value) {
        Objects.requireNonNull(value);
        this.value = normalize(value);
        this.validate(value);
    }

    /**
     * JsonValue annotation allows Jackson to serialize this value object as its underlying value,
     * rather than as an object with a "value" field.
     */
    @JsonValue
    public T getValue() {
        return value;
    }

    /**
     * Useful for converting your value to a standard form.
     * e.g. stripping whitespace, all lowercase
     */
    protected T normalize(final T originalValue) {
        // No-op unless extended
        return originalValue;
    }

    /**
     * Useful for asserting properties that must be true about your value object.
     * e.g. Can't be greater than 10, must be at least 4 characters long.
     */
    protected void validate(final T value) {
        // No-op unless extended
    }

    @Override
    @SuppressWarnings("EqualsGetClass")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Value<?> valueObject = (Value<?>) o;
        return Objects.equals(value, valueObject.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return getValue().toString();
    }

}
