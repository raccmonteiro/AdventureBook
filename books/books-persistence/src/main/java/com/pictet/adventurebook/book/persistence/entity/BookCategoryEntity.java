package com.pictet.adventurebook.book.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("book_category")
public class BookCategoryEntity {

    @Id
    @Column("book_id")
    private Long bookId;

    @Column("category_id")
    private Integer categoryId;
}
