package com.pictet.adventurebook.book.domain.type;

import com.pictet.adventurebook.common.domain.Value;

import org.apache.commons.lang3.Validate;

public final class Title extends Value<String> {

    Title(final String value) {
        super(value);
    }

    public static Title valueOf(final String value) {
        return new Title(value);
    }

    @Override
    protected void validate(String value) {
        Validate.notBlank(value, "Title can't be blank");
    }

}
