package com.pictet.adventurebook.book.svc;

import java.util.Collection;
import java.util.List;

import com.pictet.adventurebook.book.domain.Book;
import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.Difficulty;
import com.pictet.adventurebook.book.domain.type.Author;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.domain.type.Title;
import com.pictet.adventurebook.book.svc.api.BookService;
import com.pictet.adventurebook.book.svc.features.GetBookDetailsHandler;
import com.pictet.adventurebook.book.svc.features.SearchBooksHandler;
import com.pictet.adventurebook.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BookService {

    private final GetBookDetailsHandler getBookDetailsHandler;
    private final SearchBooksHandler searchBooksHandler;

    @Override
    public Result<Book> getBookDetails(BookId bookId) {
        return getBookDetailsHandler.handle(bookId);
    }

    @Override
    public Result<List<Book>> searchBooks(Title title, Author author, Collection<Category> categories, Difficulty difficulty, int page, int size) {
        return searchBooksHandler.handle(title, author, categories, difficulty, page, size);
    }
}
