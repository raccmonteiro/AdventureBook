package com.pictet.adventurebook.game.domain.type;

import com.pictet.adventurebook.common.domain.Value;

public final class SessionSectionId extends Value<Long> {

    SessionSectionId(final Long value) {
        super(value);
    }

    public static SessionSectionId valueOf(final Long value) {
        return new SessionSectionId(value);
    }

}
