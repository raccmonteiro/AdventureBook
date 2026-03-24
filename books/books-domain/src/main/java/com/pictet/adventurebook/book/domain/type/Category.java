package com.pictet.adventurebook.book.domain.type;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Category {

    FICTION(1),
    SCIENCE(2),
    HORROR(3),
    ADVENTURE(4),
    FANTASY(5),
    MYSTERY(6),
    THRILLER(7),
    ROMANCE(8),
    HISTORICAL(9);

    private final int id;

    private static final Map<Integer, Category> ID_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(Category::getId, c -> c));

    Category(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Category fromId(final int id) {
        Category category = ID_MAP.get(id);
        if (category == null) {
            throw new IllegalArgumentException("Unknown category id: " + id);
        }
        return category;
    }

}
