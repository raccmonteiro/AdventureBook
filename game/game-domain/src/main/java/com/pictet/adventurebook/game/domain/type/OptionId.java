package com.pictet.adventurebook.game.domain.type;

import com.pictet.adventurebook.common.domain.Value;

public final class OptionId extends Value<Long> {

    OptionId(final Long value) {
        super(value);
    }

    public static OptionId valueOf(final Long value) {
        return new OptionId(value);
    }

}
