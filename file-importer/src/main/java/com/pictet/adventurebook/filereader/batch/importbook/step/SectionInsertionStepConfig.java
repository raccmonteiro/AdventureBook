package com.pictet.adventurebook.filereader.batch.importbook.step;

import com.pictet.adventurebook.filereader.batch.BookImportProcessor;
import com.pictet.adventurebook.filereader.batch.importbook.dto.BookImportData;
import com.pictet.adventurebook.filereader.batch.importbook.ImportBookReaderFactory;
import com.pictet.adventurebook.filereader.batch.importbook.writer.SectionInsertionWriter;
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
 * Configuration for Step 3: Section Insertion
 * Inserts section entities into the database (without options/consequences).
 */
@Configuration
public class SectionInsertionStepConfig {

    @Bean
    @StepScope
    public JsonItemReader<ImportItem> sectionReader(@Value("#{jobParameters['fileId']}") Long fileId,
                                                   ImportBookReaderFactory readerFactory) {
        return readerFactory.createReader(fileId);
    }

    @Bean
    public Step sectionInsertionStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager,
                                     @Qualifier("sectionReader") JsonItemReader<ImportItem> sectionReader,
                                     BookImportProcessor processor,
                                     SectionInsertionWriter sectionWriter) {
        return new StepBuilder("sectionInsertionStep", jobRepository)
                .<ImportItem, BookImportData>chunk(10, transactionManager)
                .reader(sectionReader)
                .processor(processor)
                .writer(sectionWriter)
                .build();
    }
}
