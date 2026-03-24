package com.pictet.adventurebook.filereader.batch.importbook.step;

import com.pictet.adventurebook.filereader.batch.importbook.ImportBookReaderFactory;
import com.pictet.adventurebook.filereader.batch.importbook.ValidationProcessor;
import com.pictet.adventurebook.filereader.batch.importbook.writer.ValidationWriter;
import com.pictet.adventurebook.filereader.model.ImportItem;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configuration for Step 1: Validation
 * Validates JSON structure and that all gotoId references point to existing section IDs.
 */
@Configuration
public class ValidationStepConfig {

    @Bean
    public ValidationProcessor validationProcessor() {
        return new ValidationProcessor();
    }

    @Bean
    @StepScope
    public JsonItemReader<ImportItem> validationReader(@Value("#{jobParameters['fileId']}") Long fileId,
                                                      ImportBookReaderFactory readerFactory) {
        return readerFactory.createReader(fileId);
    }

    @Bean
    public Step validationStep(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager,
                               @Qualifier("validationReader") JsonItemReader<ImportItem> validationReader,
                               ValidationProcessor validationProcessor,
                               ValidationWriter validationWriter) {
        return new StepBuilder("validationStep", jobRepository)
                .<ImportItem, ValidationProcessor.ValidationData>chunk(10, transactionManager)
                .reader(validationReader)
                .processor(validationProcessor)
                .writer(validationWriter)
                .build();
    }
}
