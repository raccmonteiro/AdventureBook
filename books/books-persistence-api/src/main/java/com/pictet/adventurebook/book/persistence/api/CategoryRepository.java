package com.pictet.adventurebook.book.persistence.api;

import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.BookId;

import java.util.Collection;
import java.util.List;

/**
 * Repository for Category
 */
public interface CategoryRepository {

    /**
     * Add categories to a book
     * @param bookId
     * @param categories
     * @return the updated categories
     */
    Collection<Category> addCategories(BookId bookId, List<Category> categories);

    /**
     * Remove categories from a book
     * @param bookId
     * @param categories
     * @return the updated categories
     */
    Collection<Category> removeCategories(BookId bookId, Collection<Category> categories);

    /**
     * Retrieve categories of a book
     * @param bookId
     * @return the categories of a book
     */
    Collection<Category> findByBookId(BookId bookId);
}
