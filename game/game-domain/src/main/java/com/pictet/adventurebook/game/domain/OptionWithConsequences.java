package com.pictet.adventurebook.game.domain;


import java.util.List;

import com.pictet.adventurebook.game.domain.type.OptionDescription;
import com.pictet.adventurebook.game.domain.type.OptionId;
import com.pictet.adventurebook.game.domain.type.SectionId;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OptionWithConsequences {
    private final OptionId id;
    private final SectionId sectionId;
    private final SectionId gotoId;
    private final OptionDescription description;
    private final List<Consequence> consequences;

}
