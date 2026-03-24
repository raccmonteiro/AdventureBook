package com.pictet.adventurebook.book.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("book_file_content")
public class BookFileContentEntity {
    @Id
    private Long id;

    @Column("book_id")
    private Long bookId;

    private byte[] content;

    private LocalDateTime createdAt;
}
