package com.pictet.adventurebook.common.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValueTest {

    static class StringValue extends Value<String> {
        protected StringValue(String value) {
            super(value);
        }

        @Override
        protected String normalize(String originalValue) {
            return originalValue.trim().toLowerCase();
        }

        @Override
        protected void validate(String value) {
            if (value.length() < 3) {
                throw new IllegalArgumentException("Length must be at least 3");
            }
        }
    }

    static class SimpleValue extends Value<Integer> {
        protected SimpleValue(Integer value) {
            super(value);
        }
    }

    @Test
    void shouldCreateValue() {
        SimpleValue value = new SimpleValue(10);
        assertEquals(10, value.getValue());
    }

    @Test
    void shouldNormalizeValue() {
        StringValue value = new StringValue("  TEST  ");
        assertEquals("test", value.getValue());
    }

    @Test
    void shouldValidateValue() {
        assertThrows(IllegalArgumentException.class, () -> new StringValue("ab"));
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(NullPointerException.class, () -> new SimpleValue(null));
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        SimpleValue v1 = new SimpleValue(10);
        SimpleValue v2 = new SimpleValue(10);
        SimpleValue v3 = new SimpleValue(20);

        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
        assertNotEquals(v1, null);
        assertNotEquals(v1, "10");

        assertEquals(v1.hashCode(), v2.hashCode());
        assertNotEquals(v1.hashCode(), v3.hashCode());
    }

    @Test
    void shouldTestToString() {
        SimpleValue value = new SimpleValue(10);
        assertEquals("10", value.toString());
    }

    static class ComparableIntValue extends ComparableValue<Integer> {
        protected ComparableIntValue(Integer value) {
            super(value);
        }
    }

    @Test
    void shouldCompareValues() {
        ComparableIntValue v1 = new ComparableIntValue(10);
        ComparableIntValue v2 = new ComparableIntValue(20);
        ComparableIntValue v3 = new ComparableIntValue(10);

        assertTrue(v1.compareTo(v2) < 0);
        assertTrue(v2.compareTo(v1) > 0);
        assertEquals(0, v1.compareTo(v3));
    }

    static class NonComparableValue extends ComparableValue<Object> {
        protected NonComparableValue(Object value) {
            super(value);
        }
    }

    @Test
    void shouldThrowExceptionWhenComparingNonComparable() {
        NonComparableValue v1 = new NonComparableValue(new Object());
        NonComparableValue v2 = new NonComparableValue(new Object());
        assertThrows(UnsupportedOperationException.class, () -> v1.compareTo(v2));
    }
}
