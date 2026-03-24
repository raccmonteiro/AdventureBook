package com.pictet.adventurebook.book.controller;

import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.svc.api.BookAdminService;
import com.pictet.adventurebook.book.validator.AdminRequestValidator;
import com.pictet.adventurebook.book.validator.BookRequestValidator;
import com.pictet.adventurebook.books.model.AddBookCategoriesRequest;
import com.pictet.adventurebook.books.model.GetBookCategories200Response;
import com.pictet.adventurebook.books.model.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BooksAdminControllerTest {

    private BookAdminService bookAdminService;
    private AdminRequestValidator validator;
    private BooksAdminController controller;

    @BeforeEach
    void setUp() {
        bookAdminService = Mockito.mock(BookAdminService.class);
        validator = new AdminRequestValidator(new BookRequestValidator());
        controller = new BooksAdminController(bookAdminService, validator);
    }

    @Test
    void shouldReturnErrorWhenInvalidCategoriesProvided() {
        AddBookCategoriesRequest request = new AddBookCategoriesRequest();
        request.setCategories(List.of("FICTION", "INVALID1", "INVALID2"));
        String bookId = "123";

        ResponseEntity<GetBookCategories200Response> response = controller.addBookCategories(bookId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorBody = (ErrorResponse) response.getBody();
        assertEquals("Invalid input", errorBody.getError());
        
        String expectedMessage = "Categories: [INVALID1, INVALID2] are invalid. Available categories: " + Arrays.toString(Category.values());
        assertEquals(expectedMessage, errorBody.getMessage());
    }
}
