package com.pictet.adventurebook.book.domain.type;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum BookStatus {
    ACTIVE('A'),
    INACTIVE('I');

    private final char id;

    private static final Map<Character, BookStatus> ID_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(BookStatus::getId, d -> d));

    BookStatus(char id) {
        this.id = id;
    }

    public char getId() {
        return id;
    }

}
