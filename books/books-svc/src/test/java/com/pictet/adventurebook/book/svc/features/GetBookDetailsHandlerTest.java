package com.pictet.adventurebook.book.svc.features;

import com.pictet.adventurebook.book.domain.Book;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.persistence.api.BookRepository;
import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBookDetailsHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private GetBookDetailsHandler handler;

    @Test
    void shouldReturnBookDetails() {
        BookId bookId = BookId.valueOf(1L);
        Book book = mock(Book.class);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Result<Book> result = handler.handle(bookId);

        assertTrue(result.isSuccess());
        assertEquals(book, result.getValue());
    }

    @Test
    void shouldReturnFailureWhenBookNotFound() {
        BookId bookId = BookId.valueOf(1L);
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Result<Book> result = handler.handle(bookId);

        assertTrue(result.isFailure());
        assertEquals(ErrorCode.NOT_FOUND, result.getError().getCode());
    }
}
