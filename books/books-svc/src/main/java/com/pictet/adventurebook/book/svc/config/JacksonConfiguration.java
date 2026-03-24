package com.pictet.adventurebook.book.svc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    /**
     * Provides a default ObjectMapper bean if one is not already defined in the context.
     * This allows other components to autowire ObjectMapper without requiring explicit configuration.
     * Used for JSON validation in the {@link com.pictet.adventurebook.book.svc.features.upload.JsonValidator}
     */
    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
