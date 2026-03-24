package com.pictet.adventurebook.game.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.pictet.adventurebook.common.domain.ComparableValue;

public final class SectionId extends ComparableValue<Long> {

    SectionId(final Long value) {
        super(value);
    }

    @JsonCreator
    public static SectionId valueOf(final Long value) {
        return new SectionId(value);
    }

}
