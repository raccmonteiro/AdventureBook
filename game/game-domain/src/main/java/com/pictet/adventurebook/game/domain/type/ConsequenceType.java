package com.pictet.adventurebook.game.domain.type;

import com.pictet.adventurebook.common.domain.Value;
import org.apache.commons.lang3.Validate;

public final class ConsequenceType extends Value<String>{

    ConsequenceType(final String value) {
        super(value);
    }

    public static ConsequenceType valueOf(final String value) {
        return new ConsequenceType(value);
    }

    @Override
    protected void validate(String value) {
        Validate.notBlank(value, "Consequence Type can't be blank");
    }

}
