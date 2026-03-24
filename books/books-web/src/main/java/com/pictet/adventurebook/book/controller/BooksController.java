package com.pictet.adventurebook.book.controller;

import com.pictet.adventurebook.book.domain.Book;
import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.Difficulty;
import com.pictet.adventurebook.book.domain.type.Author;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.domain.type.Title;
import com.pictet.adventurebook.book.svc.api.BookService;
import com.pictet.adventurebook.book.validator.BookRequestValidator;
import com.pictet.adventurebook.books.api.BooksApi;
import com.pictet.adventurebook.books.model.*;
import com.pictet.adventurebook.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BooksController implements BooksApi {

    private final BookService bookService;
    private final BookRequestValidator validator;

    @Override
    public ResponseEntity<GetBook200Response> getBook(String bookId) {

        Optional<Long> maybeBookId = validator.validateAndParseBookId(bookId);

        if (maybeBookId.isEmpty()) {
            return new ResponseEntity<>(mapToErrorResponse("Invalid ID", "Invalid book ID"), HttpStatus.BAD_REQUEST);
        }
        Result<Book> result = bookService.getBookDetails(BookId.valueOf(maybeBookId.get()));
        return result.fold(
            book -> ResponseEntity.ok(mapToGetBookResponse(book)),
            error -> new ResponseEntity<>(mapToErrorResponse("Not found", error.getMessage()), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public ResponseEntity<GetBookCategories200Response> getBookCategories(String bookId) {

        Optional<Long> maybeBookId = validator.validateAndParseBookId(bookId);

        if (maybeBookId.isEmpty()) {
            return new ResponseEntity<>(mapToErrorResponse("Invalid ID", "Invalid book ID"), HttpStatus.BAD_REQUEST);
        }

        Result<Book> result = bookService.getBookDetails(BookId.valueOf(maybeBookId.get()));
        return result.fold(
            book -> {
                List<String> categories = book.categories().stream()
                                              .map(Category::name)
                                              .toList();
                GetBookCategoriesResponse response = new GetBookCategoriesResponse();
                response.setCategories(categories);
                return ResponseEntity.ok(response);
            },
            error -> new ResponseEntity<>(mapToErrorResponse("Not found", error.getMessage()), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public ResponseEntity<SearchBooks200Response> searchBooks(
            String title,
            String author,
            String category,
            String difficulty,
            Integer page,
            Integer size) {

        Title titleObj = title != null ? Title.valueOf(title) : null;
        Author authorObj = author != null ? Author.valueOf(author) : null;
        Set<Category> categories = validator.validateAndParseCategories(category).orElse(null);
        Difficulty difficultyEnum = validator.validateAndParseDifficulty(difficulty).orElse(null);

        Result<List<Book>> result = bookService.searchBooks(titleObj, authorObj, categories, difficultyEnum, page, size);

        return result.fold(
            books -> {
                SearchBooksResponse response = new SearchBooksResponse();
                response.setPageSize(size);
                response.setPageNumber(page);
                response.setBooks(books.stream()
                        .map(this::mapToBookDto)
                        .toList());

                return ResponseEntity.ok(response);
            },
            error -> new ResponseEntity<>(mapToErrorResponse("Invalid input", error.getMessage()), HttpStatus.BAD_REQUEST)
        );
    }

    private GetBookResponse mapToGetBookResponse(Book book) {
        com.pictet.adventurebook.books.model.Book bookDto = new com.pictet.adventurebook.books.model.Book()
                .title(book.title().getValue())
                .author(book.author().getValue())
                .difficulty(book.difficulty().name())
                .categories(book.categories().stream()
                        .map(Category::name)
                        .toList());

        GetBookResponse response = new GetBookResponse();
        response.setBook(bookDto);
        return response;
    }

    private com.pictet.adventurebook.books.model.Book mapToBookDto(Book book) {
        return new com.pictet.adventurebook.books.model.Book()
                .title(book.title().getValue())
                .author(book.author().getValue())
                .difficulty(book.difficulty().name())
                .categories(book.categories().stream()
                        .map(Category::name)
                        .toList());
    }

    private ErrorResponse mapToErrorResponse(String errorCode, String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(errorCode);
        errorResponse.setMessage(message);
        return errorResponse;
    }

}
