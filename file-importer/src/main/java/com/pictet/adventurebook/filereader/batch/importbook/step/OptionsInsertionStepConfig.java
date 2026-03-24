package com.pictet.adventurebook.filereader.batch.importbook.step;

import com.pictet.adventurebook.filereader.batch.BookImportProcessor;
import com.pictet.adventurebook.filereader.batch.importbook.dto.BookImportData;
import com.pictet.adventurebook.filereader.batch.importbook.ImportBookReaderFactory;
import com.pictet.adventurebook.filereader.batch.importbook.writer.OptionsInsertionWriter;
import com.pictet.adventurebook.filereader.model.ImportItem;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configuration for Step 4: Options and Consequences Insertion
 * Inserts option and consequence entities into the database.
 */
@Configuration
public class OptionsInsertionStepConfig {

    @Bean
    @StepScope
    public JsonItemReader<ImportItem> optionsReader(@Value("#{jobParameters['fileId']}") Long fileId,
                                                   ImportBookReaderFactory readerFactory) {
        return readerFactory.createReader(fileId);
    }

    @Bean
    public Step optionsInsertionStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager,
                                     @Qualifier("optionsReader") JsonItemReader<ImportItem> optionsReader,
                                     BookImportProcessor processor,
                                     OptionsInsertionWriter optionsWriter) {
        return new StepBuilder("optionsInsertionStep", jobRepository)
                .<ImportItem, BookImportData>chunk(10, transactionManager)
                .reader(optionsReader)
                .processor(processor)
                .writer(optionsWriter)
                .build();
    }
}
