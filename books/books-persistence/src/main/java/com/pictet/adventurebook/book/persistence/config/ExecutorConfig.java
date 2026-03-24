package com.pictet.adventurebook.book.persistence.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfig {

    @Bean
    Executor virtualThreadsExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
