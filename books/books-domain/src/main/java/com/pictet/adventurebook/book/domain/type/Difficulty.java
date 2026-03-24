package com.pictet.adventurebook.book.domain.type;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Difficulty {
    EASY('E'),
    MEDIUM('M'),
    HARD('H');

    private final char id;

    private static final Map<Character, Difficulty> ID_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(Difficulty::getId, d -> d));

    Difficulty(char id) {
        this.id = id;
    }

    public char getId() {
        return id;
    }

    public static Difficulty fromId(final char id) {
        Difficulty difficulty = ID_MAP.get(id);
        if (difficulty == null) {
            throw new IllegalArgumentException("Unknown difficulty id: " + id);
        }
        return difficulty;
    }

}
