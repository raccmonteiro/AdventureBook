package com.pictet.adventurebook.game.domain;


import com.pictet.adventurebook.game.domain.type.OptionDescription;
import com.pictet.adventurebook.game.domain.type.OptionId;
import com.pictet.adventurebook.game.domain.type.SectionId;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Option {
    private final OptionId id;
    private final SectionId sectionId;
    private final SectionId gotoId;
    private final OptionDescription description;

}
