package com.pictet.adventurebook.game.domain.type;

import com.pictet.adventurebook.common.domain.Value;
import org.apache.commons.lang3.Validate;

public final class ConsequenceText extends Value<String> {

    ConsequenceText(final String value) {
        super(value);
    }

    public static ConsequenceText valueOf(final String value) {
        return new ConsequenceText(value);
    }

    @Override
    protected void validate(String value) {
        Validate.notBlank(value, "Consequence Text can't be blank");
    }
}
