package com.pictet.adventurebook.game;

import jakarta.annotation.PostConstruct;

import java.time.ZoneOffset;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * SpringBootServletInitializer is used to deploy the application as a war file to an external servlet container like Tomcat.
 */
@SpringBootApplication
public class GameSessionsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GameSessionsApplication.class, args);
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        final SpringApplicationBuilder applicationBuilder = application.sources(GameSessionsApplication.class);
        // TODO setupProfile(applicationBuilder);

        return applicationBuilder;
    }


}
