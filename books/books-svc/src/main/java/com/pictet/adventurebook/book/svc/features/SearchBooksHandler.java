package com.pictet.adventurebook.book.svc.features;

import com.pictet.adventurebook.book.domain.Book;
import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.Difficulty;
import com.pictet.adventurebook.book.domain.type.Author;
import com.pictet.adventurebook.book.domain.type.Title;
import com.pictet.adventurebook.book.persistence.api.BookRepository;
import com.pictet.adventurebook.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchBooksHandler {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Result<List<Book>> handle(Title title, Author author, Collection<Category> categories, Difficulty difficulty, int page, int size) {
        return Result.success(
            bookRepository.searchBooks(title, author, categories, difficulty, page, size)
        );
    }
}
