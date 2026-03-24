package com.pictet.adventurebook.game.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum GameStatus {

    IN_PROGRESS('I'),
    WON('W'),
    LOST('L');

    private final char id;

    private static final Map<Character, GameStatus> ID_MAP =
        Arrays.stream(values())
              .collect(Collectors.toMap(GameStatus::getId, d -> d));

    GameStatus(char id) {
        this.id = id;
    }

    public char getId() {
        return id;
    }

    public static GameStatus fromId(final char id) {
        GameStatus difficulty = ID_MAP.get(id);
        if (difficulty == null) {
            throw new IllegalArgumentException("Unknown game status id: " + id);
        }
        return difficulty;
    }

}
