package com.pictet.adventurebook.book.svc.api;

import java.util.Collection;
import java.util.List;

import com.pictet.adventurebook.book.domain.Book;
import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.Difficulty;
import com.pictet.adventurebook.book.domain.type.Author;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.domain.type.Title;
import com.pictet.adventurebook.common.result.Result;

/**
 * Service for book retrieval operations, such as getting book details and searching for books.
 */
public interface BookService {

    /**
     * Get the details of a book by its id.
     * @param bookId the id of the book to retrieve
     * @return the details of the book, or an error if the book was not found
     */
    Result<Book> getBookDetails(BookId bookId);

    /**
     * Search for books by title, author, category and difficulty. All parameters are optional and can be combined to narrow down the
     * search results.
     * @param title the title of the book to search for (optional)
     * @param author the author of the book to search for (optional)
     * @param categories the categories of the book to search for (optional)
     * @param difficulty the difficulty of the book to search for (optional)
     * @param page the page number (0-indexed)
     * @param size the page size
     * @return a list of books that match the search criteria, or an error if the search failed
     */
    Result<List<Book>> searchBooks(
        Title title,
        Author author,
        Collection<Category> categories,
        Difficulty difficulty,
        int page,
        int size
    );
}
