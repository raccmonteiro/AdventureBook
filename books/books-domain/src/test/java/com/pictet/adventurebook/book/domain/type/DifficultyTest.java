package com.pictet.adventurebook.book.domain.type;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DifficultyTest {

    @ParameterizedTest
    @CsvSource({
            "EASY, E",
            "MEDIUM, M",
            "HARD, H"
    })
    void getId_shouldReturnCorrectId(Difficulty difficulty, char expectedId) {
        assertThat(difficulty.getId()).isEqualTo(expectedId);
    }

    @ParameterizedTest
    @CsvSource({
            "E, EASY",
            "M, MEDIUM",
            "H, HARD"
    })
    void fromId_shouldReturnCorrectDifficulty(char id, Difficulty expectedDifficulty) {
        assertThat(Difficulty.fromId(id)).isEqualTo(expectedDifficulty);
    }

    @Test
    void fromId_shouldThrowExceptionForInvalidId() {
        assertThatThrownBy(() -> Difficulty.fromId('X'))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown difficulty id: X");
    }

    @Test
    void fromId_shouldThrowExceptionForLowercaseLetter() {
        assertThatThrownBy(() -> Difficulty.fromId('e'))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown difficulty id: e");
    }

    @Test
    void fromId_shouldThrowExceptionForNumber() {
        assertThatThrownBy(() -> Difficulty.fromId('1'))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown difficulty id: 1");
    }

    @Test
    void allDifficulties_shouldHaveUniqueIds() {
        long uniqueIdCount = java.util.Arrays.stream(Difficulty.values())
                .map(Difficulty::getId)
                .distinct()
                .count();

        assertThat(uniqueIdCount).isEqualTo(Difficulty.values().length);
    }
}
