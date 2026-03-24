package com.pictet.adventurebook.book.domain.type;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryTest {

    @ParameterizedTest
    @CsvSource({
            "FICTION, 1",
            "SCIENCE, 2",
            "HORROR, 3",
            "ADVENTURE, 4",
            "FANTASY, 5",
            "MYSTERY, 6",
            "THRILLER, 7",
            "ROMANCE, 8",
            "HISTORICAL, 9"
    })
    void getId_shouldReturnCorrectId(Category category, int expectedId) {
        assertThat(category.getId()).isEqualTo(expectedId);
    }

    @ParameterizedTest
    @CsvSource({
            "1, FICTION",
            "2, SCIENCE",
            "3, HORROR",
            "4, ADVENTURE",
            "5, FANTASY",
            "6, MYSTERY",
            "7, THRILLER",
            "8, ROMANCE",
            "9, HISTORICAL"
    })
    void fromId_shouldReturnCorrectCategory(int id, Category expectedCategory) {
        assertThat(Category.fromId(id)).isEqualTo(expectedCategory);
    }

    @Test
    void fromId_shouldThrowExceptionForInvalidId() {
        assertThatThrownBy(() -> Category.fromId(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown category id: 999");
    }

    @Test
    void fromId_shouldThrowExceptionForZeroId() {
        assertThatThrownBy(() -> Category.fromId(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown category id: 0");
    }

    @Test
    void fromId_shouldThrowExceptionForNegativeId() {
        assertThatThrownBy(() -> Category.fromId(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown category id: -1");
    }

    @Test
    void allCategories_shouldHaveUniqueIds() {
        long uniqueIdCount = java.util.Arrays.stream(Category.values())
                .map(Category::getId)
                .distinct()
                .count();

        assertThat(uniqueIdCount).isEqualTo(Category.values().length);
    }
}
