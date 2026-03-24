package com.pictet.adventurebook.game.domain.type;

import com.pictet.adventurebook.common.domain.Value;

public final class ConsequenceId extends Value<Long> {

    ConsequenceId(final Long value) {
        super(value);
    }

    public static ConsequenceId valueOf(final Long value) {
        return new ConsequenceId(value);
    }

}
