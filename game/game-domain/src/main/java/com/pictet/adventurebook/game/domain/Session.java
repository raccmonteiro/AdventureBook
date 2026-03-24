package com.pictet.adventurebook.game.domain;

import com.pictet.adventurebook.game.domain.type.SectionId;
import com.pictet.adventurebook.game.domain.type.SessionId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class Session {

    private final SessionId id;

    @Setter
    private SectionId sectionId;

    @Setter
    private SessionState state;

    @Setter
    private GameStatus status;

}

