package com.pictet.adventurebook.book.svc.api;

import java.util.Collection;
import java.util.List;

import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.common.result.Result;

import java.io.InputStream;

/**
 * Service for book administration operations, such as adding/removing categories and uploading books.
 */
public interface BookAdminService {

    /**
     * Add categories to a book. If a category already exists for the book, it will be ignored.
     * @param bookId the id of the book to add categories to
     * @param categories the categories to add
     * @return the updated list of categories for the book
     */
    Result<List<Category>> addCategory(BookId bookId, List<Category> categories);

    /**
     * Remove categories from a book. If a category does not exist for the book, it will be ignored.
     * @param bookId the id of the book to remove categories from
     * @param categories the categories to remove
     * @return the updated list of categories for the book
     */
    Result<List<Category>> removeCategory(BookId bookId, Collection<Category> categories);

    /**
     * Upload a book from an input stream. The input stream should contain a valid book in the expected format (e.g. JSON, XML).
     * @param inputStream the input stream containing the book to upload
     * @return void if the upload was successful, or an error if the upload failed
     */
    Result<Void> uploadBook(InputStream inputStream);

}
