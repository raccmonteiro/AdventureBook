package com.pictet.adventurebook.game.domain.type;

import com.pictet.adventurebook.common.domain.Value;
import org.apache.commons.lang3.Validate;

public final class OptionDescription extends Value<String> {

    OptionDescription(final String value) {
        super(value);
    }

    public static OptionDescription valueOf(final String value) {
        return new OptionDescription(value);
    }

    @Override
    protected void validate(String value) {
        Validate.notBlank(value, "Option Description can't be blank");
    }
}
