package com.pictet.adventurebook.game.persistence.repository.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventurebook.game.domain.SessionState;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@WritingConverter
@RequiredArgsConstructor
public class SessionStateWritingConverter implements Converter<SessionState, PGobject> {

    private final ObjectMapper objectMapper;

    @Override
    public PGobject convert(SessionState source) {
        try {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("jsonb");
            jsonObject.setValue(objectMapper.writeValueAsString(source));
            return jsonObject;
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("Error converting SessionState to PGobject", e);
        }
    }
}
