package com.pictet.adventurebook.book.domain.type;

import com.pictet.adventurebook.common.domain.ComparableValue;

public final class BookId extends ComparableValue<Long> {

    BookId(final Long value) {
        super(value);
    }

    public static BookId valueOf(final Long value) {
        return new BookId(value);
    }

}
