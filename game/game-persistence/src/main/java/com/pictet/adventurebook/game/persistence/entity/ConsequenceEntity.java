package com.pictet.adventurebook.game.persistence.entity;

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
@Table("consequence")
public class ConsequenceEntity {
    @Id
    private Long id;

    @Column("option_id")
    private Long optionId;

    private String type;

    private Double value;

    private String text;
}
