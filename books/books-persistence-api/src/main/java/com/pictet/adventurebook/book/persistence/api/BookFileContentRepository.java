package com.pictet.adventurebook.book.persistence.api;

import java.io.InputStream;

/**
 * Repository for BookFile content
 */
public interface BookFileContentRepository {

    /**
     * Save a book file to the repository
     * @param inputStream the input stream of the book file
     * @return the id of the saved book file
     */
    Long save(InputStream inputStream);

    /**
     * Find the content of a book file by its id
     * @param id the id of the book file
     * @return the input stream of the book file
     */
    InputStream findContentById(Long id);

    /**
     * Update the book id of a book file
     * @param fileId the id of the book file
     * @param bookId the id of the book
     */
    void updateBookId(Long fileId, Long bookId);
}
