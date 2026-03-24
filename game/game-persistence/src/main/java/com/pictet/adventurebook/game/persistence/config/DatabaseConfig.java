package com.pictet.adventurebook.game.persistence.config;

import com.pictet.adventurebook.game.persistence.repository.converter.SessionStateReadingConverter;
import com.pictet.adventurebook.game.persistence.repository.converter.SessionStateWritingConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Configuration
@EnableJdbcRepositories(basePackages = "com.pictet.adventurebook.game.persistence.repository.spring")
@EnableTransactionManagement
@RequiredArgsConstructor
public class DatabaseConfig extends AbstractJdbcConfiguration {

    private final SessionStateWritingConverter sessionStateWritingConverter;
    private final SessionStateReadingConverter sessionStateReadingConverter;

    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(List.of(
            sessionStateWritingConverter,
            sessionStateReadingConverter
        ));
    }

}
