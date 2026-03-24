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

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveCategoryHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private RemoveCategoryHandler handler;

    @Test
    void shouldRemoveCategories() {
        BookId bookId = BookId.valueOf(1L);
        Collection<Category> categoriesToRemove = List.of(Category.FICTION);
        List<Category> remainingCategories = List.of(Category.SCIENCE);
        
        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(categoryRepository.removeCategories(bookId, categoriesToRemove)).thenReturn(remainingCategories);

        Result<List<Category>> result = handler.handle(bookId, categoriesToRemove);

        assertTrue(result.isSuccess());
        assertEquals(remainingCategories, result.getValue());
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
