package com.pictet.adventurebook.book.domain;

import java.util.List;

import com.pictet.adventurebook.book.domain.type.Author;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.Difficulty;
import com.pictet.adventurebook.book.domain.type.Title;

public record Book(
        BookId id,
        Title title,
        Author author,
        Difficulty difficulty,
        List<Category> categories
) {
}
