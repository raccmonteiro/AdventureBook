package com.pictet.adventurebook.game.domain;

import java.util.List;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class SessionMaterialized extends Session {
    private final SectionWithOptions sectionWithOptions;
    private final List<Consequence> consequences; //from previous choice if any

}

