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
@Table("book")
public class BookEntity {
    @Id
    private Long id;

    private String title;

    private String author;

    @Column("difficulty_id")
    private Character difficultyId;

    @Column("status")
    private Character status;

}
