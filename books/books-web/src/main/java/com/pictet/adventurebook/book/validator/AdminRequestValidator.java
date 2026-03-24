package com.pictet.adventurebook.book.validator;

import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.books.model.AddBookCategoriesRequest;
import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class AdminRequestValidator {

    private final BookRequestValidator bookRequestValidator;

    public AdminRequestValidator(BookRequestValidator bookRequestValidator) {
        this.bookRequestValidator = bookRequestValidator;
    }

    public Optional<Long> validateAndParseBookId(String bookId) {
        return bookRequestValidator.validateAndParseBookId(bookId);
    }

    public Optional<Category> validateAndParseCategory(String categoryId) {
        return bookRequestValidator.validateAndParseCategory(categoryId);
    }

    public Result<List<Category>> validateCategories(AddBookCategoriesRequest request) {
        if (request == null || request.getCategories() == null || request.getCategories().isEmpty()) {
            return Result.success(Collections.emptyList());
        }

        List<String> invalidCategories = request.getCategories().stream()
                .filter(category -> bookRequestValidator.validateAndParseCategory(category).isEmpty())
                .toList();

        if (!invalidCategories.isEmpty()) {
            String message = String.format("Categories: %s are invalid. Available categories: %s",
                    invalidCategories,
                    Arrays.toString(Category.values()));
            return Result.failure(ErrorCode.INVALID_INPUT, message);
        }

        List<Category> categories = request.getCategories().stream()
                .map(bookRequestValidator::validateAndParseCategory)
                .map(Optional::get)
                .toList();

        return Result.success(categories);
    }

    public boolean isValidRequest(AddBookCategoriesRequest request) {
        return request != null
                && request.getCategories() != null
                && !request.getCategories().isEmpty();
    }
}
