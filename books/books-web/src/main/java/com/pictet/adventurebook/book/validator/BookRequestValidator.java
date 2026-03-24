package com.pictet.adventurebook.book.validator;

import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.Difficulty;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class BookRequestValidator {

    public Optional<Long> validateAndParseBookId(String bookId) {
        if (bookId == null || bookId.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Long.parseLong(bookId));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<Category> validateAndParseCategory(String category) {
        if (category == null || category.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Category.valueOf(category.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public Optional<Set<Category>> validateAndParseCategories(String category) {
        return validateAndParseCategory(category)
                .map(Set::of);
    }

    public Optional<Difficulty> validateAndParseDifficulty(String difficulty) {
        if (difficulty == null || difficulty.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Difficulty.valueOf(difficulty.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
