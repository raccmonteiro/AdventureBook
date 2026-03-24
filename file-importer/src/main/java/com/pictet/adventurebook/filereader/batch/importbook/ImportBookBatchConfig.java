package com.pictet.adventurebook.filereader.batch.importbook;

import com.pictet.adventurebook.filereader.batch.BookImportProcessor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration for the Import Book batch job.
 * Chains together 5 steps:
 * 1. Validation - validates JSON and all gotoId references
 * 2. Book Insertion - inserts book entities
 * 3. Section Insertion - inserts section entities
 * 4. Options Insertion - inserts option and consequence entities
 * 5. Book Activation - updates the book status to ACTIVE
 *
 */
@Configuration
public class ImportBookBatchConfig {

    /**
     * Singleton bean to maintain state (currentBookId and sectionIdMap)
     * across all steps within a single job execution.
     * The fileId is initialized via ImportBookJobListener.
     */
    @Bean
    public BookImportProcessor bookImportProcessor() {
        return new BookImportProcessor();
    }

    @Bean
    public Job importBookJob(JobRepository jobRepository,
                            @Qualifier("validationStep") Step validationStep,
                            @Qualifier("bookInsertionStep") Step bookInsertionStep,
                            @Qualifier("sectionInsertionStep") Step sectionInsertionStep,
                            @Qualifier("optionsInsertionStep") Step optionsInsertionStep,
                            @Qualifier("bookActivationStep") Step bookActivationStep) {
        return new JobBuilder("importBookJob", jobRepository)
                .start(validationStep)
                .next(bookInsertionStep)
                .next(sectionInsertionStep)
                .next(optionsInsertionStep)
                .next(bookActivationStep)
                .build();
    }
}
