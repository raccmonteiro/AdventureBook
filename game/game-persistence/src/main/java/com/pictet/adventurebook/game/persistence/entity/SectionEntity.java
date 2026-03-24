package com.pictet.adventurebook.game.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("section")
public class SectionEntity {
    @Id
    @Setter
    private Long id;

    @Column("book_id")
    @Setter
    private Long bookId;

    private Character type;

    private String text;

    @MappedCollection(idColumn = "section_id")
    @Builder.Default
    @Setter
    private Set<OptionEntity> options = new HashSet<>();

}
