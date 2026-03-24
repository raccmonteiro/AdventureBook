package com.pictet.adventurebook.common.domain;


import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * ComparableValue is a Value that can be compared to other ComparableValue objects of the same type.
 *
 */
public abstract class ComparableValue<T> extends Value<T> implements Comparable<ComparableValue<T>> {


    protected ComparableValue(@Nonnull final T value) {
        super(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(ComparableValue<T> other) {
        if (this.getValue() instanceof Comparable) {
            return ((Comparable<T>) this.getValue()).compareTo(other.getValue());
        }
        throw new UnsupportedOperationException(
            "Cannot compare values of type " + this.getValue().getClass().getName() +
            " as it does not implement Comparable"
        );
    }
}
