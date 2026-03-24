package com.pictet.adventurebook.book.svc.features;

import com.pictet.adventurebook.book.domain.Book;
import com.pictet.adventurebook.book.domain.type.Author;
import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.Difficulty;
import com.pictet.adventurebook.book.domain.type.Title;
import com.pictet.adventurebook.book.persistence.api.BookRepository;
import com.pictet.adventurebook.common.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchBooksHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private SearchBooksHandler handler;

    @Test
    void shouldSearchBooks() {
        Title title = Title.valueOf("Title");
        Author author = Author.valueOf("Author");
        Collection<Category> categories = List.of(Category.FICTION);
        Difficulty difficulty = Difficulty.EASY;
        int page = 0;
        int size = 10;

        List<Book> books = List.of(mock(Book.class));
        when(bookRepository.searchBooks(title, author, categories, difficulty, page, size)).thenReturn(books);

        Result<List<Book>> result = handler.handle(title, author, categories, difficulty, page, size);

        assertTrue(result.isSuccess());
        assertEquals(books, result.getValue());
        verify(bookRepository).searchBooks(title, author, categories, difficulty, page, size);
    }
}
