package com.pictet.adventurebook.book.persistence.repository.spring;

import com.pictet.adventurebook.book.persistence.entity.BookCategoryEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategorySpringRepository extends CrudRepository<BookCategoryEntity, Long> {

    @Query("SELECT * FROM book_category WHERE book_id = :bookId")
    List<BookCategoryEntity> findAllByBookId(@Param("bookId") Long bookId);

    @Modifying
    @Query("INSERT INTO book_category (book_id, category_id) VALUES (:bookId, :categoryId) ON CONFLICT DO NOTHING")
    void insert(@Param("bookId") Long bookId, @Param("categoryId") Integer categoryId);

    @Modifying
    @Query("DELETE FROM book_category WHERE book_id = :bookId AND category_id = :categoryId")
    void deleteByBookIdAndCategoryId(@Param("bookId") Long bookId, @Param("categoryId") Integer categoryId);
}
