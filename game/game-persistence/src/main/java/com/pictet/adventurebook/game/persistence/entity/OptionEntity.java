package com.pictet.adventurebook.game.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("option")
public class OptionEntity {
    @Id
    private Long id;

    @Column("section_id")
    private Long sectionId;

    @Column("goto_id")
    private Long gotoId;

    private String description;

    @MappedCollection(idColumn = "option_id")
    @Builder.Default
    private Set<ConsequenceEntity> consequences = new HashSet<>();
}
