package com.pictet.adventurebook.filereader.batch.importbook.step;

import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configuration for Step 5: Book Activation
 * Updates the book status to ACTIVE after all parts of the book are inserted.
 */
@Configuration
public class BookActivationStepConfig {

    @Bean
    public Step bookActivationStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  BookActivationTasklet activationTasklet) {
        return new StepBuilder("bookActivationStep", jobRepository)
                .tasklet(activationTasklet, transactionManager)
                .build();
    }
}
