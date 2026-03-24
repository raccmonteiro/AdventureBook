package com.pictet.adventurebook.book.persistence.config;



import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJdbcRepositories(basePackages = "com.pictet.adventurebook.book.persistence.repository.spring")
@EnableTransactionManagement
public class JdbcConfiguration {

}
