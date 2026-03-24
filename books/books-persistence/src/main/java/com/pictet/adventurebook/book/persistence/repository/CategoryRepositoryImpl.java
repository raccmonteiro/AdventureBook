package com.pictet.adventurebook.book.persistence.repository;

import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.persistence.api.CategoryRepository;
import com.pictet.adventurebook.book.persistence.repository.spring.CategorySpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategorySpringRepository categorySpringRepository;

    @Override
    @Transactional
    public Collection<Category> addCategories(BookId bookId, List<Category> categories) {
        categories.forEach(category -> {
            categorySpringRepository.insert(bookId.getValue(), category.getId());
        });

        return findByBookId(bookId);
    }

    @Override
    @Transactional
    public Collection<Category> removeCategories(BookId bookId, Collection<Category> categories) {
        categories.forEach(category ->
                categorySpringRepository.deleteByBookIdAndCategoryId(bookId.getValue(), category.getId())
        );

        return findByBookId(bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Category> findByBookId(BookId bookId) {
        return categorySpringRepository.findAllByBookId(bookId.getValue()).stream()
                                       .map(bc -> Category.fromId(bc.getCategoryId()))
                                       .toList();
    }
}
