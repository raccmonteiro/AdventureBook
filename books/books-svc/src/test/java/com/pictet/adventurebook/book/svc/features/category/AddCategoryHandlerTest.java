package com.pictet.adventurebook.book.svc.features.category;

import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.persistence.api.BookRepository;
import com.pictet.adventurebook.book.persistence.api.CategoryRepository;
import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddCategoryHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private AddCategoryHandler handler;

    @Test
    void shouldAddCategories() {
        BookId bookId = BookId.valueOf(1L);
        List<Category> categories = List.of(Category.FICTION, Category.SCIENCE);
        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(categoryRepository.addCategories(bookId, categories)).thenReturn(categories);

        Result<List<Category>> result = handler.handle(bookId, categories);

        assertTrue(result.isSuccess());
        assertEquals(2, result.getValue().size());
        assertEquals(Category.FICTION, result.getValue().get(0));
        assertEquals(Category.SCIENCE, result.getValue().get(1));
    }

    @Test
    void shouldReturnFailureWhenBookNotFound() {
        BookId bookId = BookId.valueOf(1L);
        when(bookRepository.existsById(bookId)).thenReturn(false);

        Result<List<Category>> result = handler.handle(bookId, List.of());

        assertTrue(result.isFailure());
        assertEquals(ErrorCode.NOT_FOUND, result.getError().getCode());
    }
}
