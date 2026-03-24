package com.pictet.adventurebook.filereader.batch.importbook.step;

import com.pictet.adventurebook.filereader.batch.BookImportProcessor;
import com.pictet.adventurebook.filereader.batch.importbook.dto.BookImportData;
import com.pictet.adventurebook.filereader.batch.importbook.ImportBookReaderFactory;
import com.pictet.adventurebook.filereader.batch.importbook.writer.BookInsertionWriter;
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
 * Configuration for Step 2: Book Insertion
 * Inserts book entities into the database.
 */
@Configuration
public class BookInsertionStepConfig {

    @Bean
    @StepScope
    public JsonItemReader<ImportItem> bookReader(@Value("#{jobParameters['fileId']}") Long fileId,
                                                ImportBookReaderFactory readerFactory) {
        return readerFactory.createReader(fileId);
    }

    @Bean
    public Step bookInsertionStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  @Qualifier("bookReader") JsonItemReader<ImportItem> bookReader,
                                  BookImportProcessor processor,
                                  BookInsertionWriter bookWriter) {
        return new StepBuilder("bookInsertionStep", jobRepository)
                .<ImportItem, BookImportData>chunk(10, transactionManager)
                .reader(bookReader)
                .processor(processor)
                .writer(bookWriter)
                .build();
    }
}
