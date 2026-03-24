package com.pictet.adventurebook.book.persistence.api;

import com.pictet.adventurebook.book.domain.Book;
import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.Difficulty;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.domain.type.Author;
import com.pictet.adventurebook.book.domain.type.Title;

import java.util.Collection;
import java.util.List;import java.util.Optional;

/**
 * Repository for Book details (title, author, categories, difficulty, etc.)
 */
public interface BookRepository {

    /**
     * Retrieve a book by its id
     * @param id
     * @return the book
     */
    Optional<Book> findById(BookId id);

    /**
     * Search for books by title, author, categories, and difficulty
     * @param title the title of the book
     * @param author the author of the book
     * @param categories the categories of the book
     * @param difficulty the difficulty of the book
     * @param page the page number (0-indexed)
     * @param size the page size
     * @return a list of books matching the search criteria
     */
    List<Book> searchBooks(Title title, Author author, Collection<Category> categories, Difficulty difficulty, int page, int size);

    /**
     * Check if a book exists by its id
     * @param id
     * @return true if the book exists, false otherwise
     */
    boolean existsById(BookId id);
}
