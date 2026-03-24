package com.pictet.adventurebook.book.domain.type;

import com.pictet.adventurebook.common.domain.Value;
import org.apache.commons.lang3.Validate;

public final class Author extends Value<String> {

    Author(final String value) {
        super(value);
    }

    public static Author valueOf(final String value) {
        return new Author(value);
    }

    @Override
    protected void validate(String value) {
        Validate.notBlank(value, "Author can't be blank");
    }

}
