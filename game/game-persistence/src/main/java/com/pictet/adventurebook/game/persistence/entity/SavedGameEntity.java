package com.pictet.adventurebook.game.persistence.entity;

import com.pictet.adventurebook.game.domain.SessionState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("saved_game")
public class SavedGameEntity {
    @Id
    private Long id;

    @Column("section_id")
    private Long sectionId;

    @Column("state")
    private SessionState state;

    @Column("status")
    private Character status;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;
}
