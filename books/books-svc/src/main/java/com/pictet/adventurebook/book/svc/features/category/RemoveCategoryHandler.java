package com.pictet.adventurebook.book.svc.features.category;

import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.persistence.api.CategoryRepository;
import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RemoveCategoryHandler {

    private final com.pictet.adventurebook.book.persistence.api.BookRepository bookRepository;
    private final CategoryRepository categoryRepository;


    @Transactional
    public Result<List<Category>> handle(BookId bookId, Collection<Category> categories) {
        if (!bookRepository.existsById(bookId)) {
            return Result.failure(ErrorCode.NOT_FOUND, "Book not found");
        }

        return Result.success(
            categoryRepository.removeCategories(bookId, categories)
                                 .stream()
                                 .sorted( Comparator.comparingInt(Category::getId) )
                                 .toList()
        );
    }
}
