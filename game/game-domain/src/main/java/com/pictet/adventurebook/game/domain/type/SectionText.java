package com.pictet.adventurebook.game.domain.type;

import com.pictet.adventurebook.common.domain.Value;
import org.apache.commons.lang3.Validate;

public final class SectionText extends Value<String> {

    SectionText(final String value) {
        super(value);
    }

    public static SectionText valueOf(final String value) {
        return new SectionText(value);
    }

    @Override
    protected void validate(String value) {
        Validate.notBlank(value, "Section Text can't be blank");
    }
}
