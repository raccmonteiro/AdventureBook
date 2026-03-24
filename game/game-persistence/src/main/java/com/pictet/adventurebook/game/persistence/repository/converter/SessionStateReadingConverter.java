package com.pictet.adventurebook.game.persistence.repository.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventurebook.game.domain.SessionState;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ReadingConverter
@RequiredArgsConstructor
public class SessionStateReadingConverter implements Converter<PGobject, SessionState> {

    private final ObjectMapper objectMapper;

    @Override
    public SessionState convert(PGobject source) {
        try {
            return objectMapper.readValue(source.getValue(), SessionState.class);
        } catch (IOException e) {
            throw new RuntimeException("Error reading SessionState from PGobject", e);
        }
    }
}
