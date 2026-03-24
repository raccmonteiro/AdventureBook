package com.pictet.adventurebook.game.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum SectionType {
    BEGIN('B'),
    END('E'),
    NODE('N');

    private final char id;

    private static final Map<Character, SectionType> ID_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(SectionType::getId, d -> d));

    SectionType(char id) {
        this.id = id;
    }

    public char getId() {
        return id;
    }

    public static SectionType fromId(final char id) {
        SectionType type = ID_MAP.get(id);
        if (type == null) {
            throw new IllegalArgumentException("Unknown type id: " + id);
        }
        return type;
    }

}
