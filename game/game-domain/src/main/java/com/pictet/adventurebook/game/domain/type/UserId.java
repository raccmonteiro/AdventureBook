package com.pictet.adventurebook.game.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.pictet.adventurebook.common.domain.Value;

public final class UserId extends Value<String> {

    UserId(final String value) {
        super(value);
    }

    @JsonCreator
    public static UserId valueOf(final String value) {
        return new UserId(value);
    }

}
