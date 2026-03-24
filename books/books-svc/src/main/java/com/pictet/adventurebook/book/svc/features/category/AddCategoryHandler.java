package com.pictet.adventurebook.book.svc.features.category;

import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.persistence.api.CategoryRepository;
import com.pictet.adventurebook.book.persistence.api.BookRepository;
import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AddCategoryHandler {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Result<List<Category>> handle(BookId bookId, List<Category> categories) {
        if (! bookRepository.existsById(bookId)) {
            return Result.failure(ErrorCode.NOT_FOUND, "Book not found");
        }

        return Result.success(
            categoryRepository.addCategories(bookId, categories)
                              .stream()
                              .sorted( Comparator.comparingInt(Category::getId) )
                              .toList()
        );
    }
}
