package com.pictet.adventurebook.book.svc.features;

import com.pictet.adventurebook.book.domain.Book;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.persistence.api.BookRepository;
import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetBookDetailsHandler {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "book", key = "#bookId.value")
    public Result<Book> handle(BookId bookId) {
        return bookRepository.findById(bookId).map(Result::success)
            .orElse(Result.failure(ErrorCode.NOT_FOUND, "Book not found"));
    }

}
