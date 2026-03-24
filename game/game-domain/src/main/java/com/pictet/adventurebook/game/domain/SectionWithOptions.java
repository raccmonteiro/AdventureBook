package com.pictet.adventurebook.game.domain;


import java.util.List;

import com.pictet.adventurebook.game.domain.type.SectionId;
import com.pictet.adventurebook.game.domain.type.SectionText;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SectionWithOptions {
    private final SectionId id;
    private final SectionText text;
    private final SectionType type;
    private final List<Option> options;

}

