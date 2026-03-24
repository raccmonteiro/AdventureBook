package com.pictet.adventurebook.game.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.pictet.adventurebook.common.domain.Value;

public final class SessionId extends Value<Long> {

    SessionId(final Long value) {
        super(value);
    }

    @JsonCreator
    public static SessionId valueOf(final Long value) {
        return new SessionId(value);
    }

}
