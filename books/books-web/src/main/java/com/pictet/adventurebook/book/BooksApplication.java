package com.pictet.adventurebook.book;

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
@SpringBootApplication(scanBasePackages = {"com.pictet.adventurebook.book", "com.pictet.adventurebook.filereader", "com.pictet.adventurebook.game"})
public class BooksApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BooksApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        final SpringApplicationBuilder applicationBuilder = application.sources(BooksApplication.class);
        // TODO setupProfile(applicationBuilder);

        return applicationBuilder;
    }

}
