package com.pictet.adventurebook.book.controller;

import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.svc.api.BookAdminService;
import com.pictet.adventurebook.book.validator.AdminRequestValidator;
import com.pictet.adventurebook.books.api.BooksAdminApi;
import com.pictet.adventurebook.books.model.*;
import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BooksAdminController implements BooksAdminApi {

    private final BookAdminService bookAdminService;
    private final AdminRequestValidator validator;

    @Override
    public ResponseEntity<GetBookCategories200Response> addBookCategories(String bookId, AddBookCategoriesRequest request) {
        if (!validator.isValidRequest(request)) {
            return new ResponseEntity<>(mapToErrorResponse("Invalid input", "Categories must not be empty"), HttpStatus.BAD_REQUEST);
        }

        Optional<Long> maybeId = validator.validateAndParseBookId(bookId);
        if (maybeId.isEmpty()) {
            return new ResponseEntity<>(mapToErrorResponse("Invalid ID", "Invalid book ID"), HttpStatus.BAD_REQUEST);
        }

        return validator.validateCategories(request).fold(
                categories -> {
                    Result<List<Category>> result = bookAdminService.addCategory(BookId.valueOf(maybeId.get()), categories);
                    return result.fold(
                            categoryList -> {
                                GetBookCategoriesResponse response = new GetBookCategoriesResponse();
                                response.setCategories(categoryList.stream().map(Category::name).toList());
                                return ResponseEntity.ok(response);
                            },
                            error -> {
                                if (error.getCode() == ErrorCode.NOT_FOUND) {
                                    return new ResponseEntity<>(mapToErrorResponse("Not found", error.getMessage()), HttpStatus.NOT_FOUND);
                                }
                                return new ResponseEntity<>(mapToErrorResponse("Invalid input", error.getMessage()), HttpStatus.BAD_REQUEST);
                            }
                    );
                },
                error -> new ResponseEntity<>(mapToErrorResponse("Invalid input", error.getMessage()), HttpStatus.BAD_REQUEST)
        );
    }

    @Override
    public ResponseEntity<GetBookCategories200Response> removeBookCategory(String bookId, String categoryId) {
        Optional<Long> maybeBookId = validator.validateAndParseBookId(bookId);
        if (maybeBookId.isEmpty()) {
            return new ResponseEntity<>(mapToErrorResponse("Invalid ID", "Invalid book ID"), HttpStatus.BAD_REQUEST);
        }

        Optional<Category> maybeCategory = validator.validateAndParseCategory(categoryId);
        if (maybeCategory.isEmpty()) {
            return new ResponseEntity<>(mapToErrorResponse("Invalid ID", "Invalid category ID"), HttpStatus.BAD_REQUEST);
        }

        Result<List<Category>> result = bookAdminService.removeCategory(BookId.valueOf(maybeBookId.get()), List.of(maybeCategory.get()));
        return result.fold(
                categoryList -> {
                    GetBookCategoriesResponse response = new GetBookCategoriesResponse();
                    response.setCategories(categoryList.stream().map(Category::name).toList());
                    return ResponseEntity.ok(response);
                },
                error -> {
                    if (error.getCode() == ErrorCode.NOT_FOUND) {
                        return new ResponseEntity<>(mapToErrorResponse("Not found", error.getMessage()), HttpStatus.NOT_FOUND);
                    }
                    return new ResponseEntity<>(mapToErrorResponse("Invalid input", error.getMessage()), HttpStatus.BAD_REQUEST);
                }
        );
    }

    @Override
    public ResponseEntity<UploadBook202Response> uploadBook(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>(mapToErrorResponse("Missing file", "File is required"), HttpStatus.BAD_REQUEST);
        }

        try(InputStream is = file.getInputStream()) {
            Result<Void> result = bookAdminService.uploadBook(is);

            return result.fold(
                v -> ResponseEntity.accepted().build(),
                error -> new ResponseEntity<>(mapToErrorResponse("Invalid input", error.getMessage()), HttpStatus.BAD_REQUEST)
            );
        } catch (IOException e) {
            log.error("Error reading InputStream", e);
            return new ResponseEntity<>(mapToErrorResponse("Read error", "Error reading uploaded file"), HttpStatus.BAD_REQUEST);
        }
    }

    private ErrorResponse mapToErrorResponse(String error, String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(error);
        errorResponse.setMessage(message);
        return errorResponse;
    }

}
