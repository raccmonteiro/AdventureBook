package com.pictet.adventurebook.book.svc.features.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonValidator {

    private final ObjectMapper objectMapper;

    /**
     * Validates if the given input stream contains valid JSON.
     */
    public boolean isValidJson(InputStream inputStream) {
        try {
            JsonParser parser = objectMapper.getFactory().createParser(inputStream);
            if (parser.nextToken() == null) {
                return false;
            }
            while (parser.nextToken() != null) {
                // Just consume tokens to validate structure
            }
            return true;
        } catch (IOException e) {
            log.debug("JSON validation failed", e);
            return false;
        }
    }



}
