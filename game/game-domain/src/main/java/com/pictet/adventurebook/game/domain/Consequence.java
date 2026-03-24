package com.pictet.adventurebook.game.domain;

import com.pictet.adventurebook.game.domain.type.ConsequenceId;
import com.pictet.adventurebook.game.domain.type.ConsequenceText;
import com.pictet.adventurebook.game.domain.type.ConsequenceType;
import com.pictet.adventurebook.game.domain.type.ConsequenceValue;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Consequence {

    private final ConsequenceId id;
    private final ConsequenceType type;
    private final ConsequenceValue value;
    private final ConsequenceText text;

}
