package com.pictet.adventurebook.game.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.pictet.adventurebook.common.domain.Value;

public final class ConsequenceValue extends Value<Double> {

    ConsequenceValue(final Double value) {
        super(value);
    }

    @JsonCreator
    public static ConsequenceValue valueOf(final Double value) {
        return new ConsequenceValue(value);
    }

}
