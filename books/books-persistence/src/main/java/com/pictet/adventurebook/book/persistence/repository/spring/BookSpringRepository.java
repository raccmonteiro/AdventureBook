package com.pictet.adventurebook.book.persistence.repository.spring;

import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.persistence.entity.BookEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookSpringRepository extends CrudRepository<BookEntity, Long> {

    @Modifying
    @Query("UPDATE book SET status = :status WHERE id = :id")
    void updateBookStatus(@Param("id") Long id, @Param("status") Character status);

    @Query("""
        SELECT DISTINCT b.*
        FROM book b
        LEFT JOIN book_category bc ON b.id = bc.book_id
        WHERE b.status = 'A'
          AND (CAST(:title AS TEXT) IS NULL OR to_tsvector('english', b.title) @@ plainto_tsquery('english', :title))
          AND (CAST(:author AS TEXT) IS NULL OR to_tsvector('english', b.author) @@ plainto_tsquery('english', :author))
          AND (CAST(:difficultyId AS CHAR) IS NULL OR b.difficulty_id = CAST(:difficultyId AS CHAR))
          AND (COALESCE(ARRAY_LENGTH(CAST(:categoryIds AS INTEGER[]), 1), 0) = 0 OR bc.category_id IN (:categoryIds))
        ORDER BY b.id
        LIMIT :limit OFFSET :offset
        """)
    List<BookEntity> searchBooks(
            @Param("title") String title,
            @Param("author") String author,
            @Param("difficultyId") Character difficultyId,
            @Param("categoryIds") Collection<Integer> categoryIds,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
