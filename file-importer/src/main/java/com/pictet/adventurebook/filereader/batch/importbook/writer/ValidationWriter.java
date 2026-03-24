package com.pictet.adventurebook.filereader.batch.importbook.writer;

import com.pictet.adventurebook.filereader.batch.importbook.ValidationProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Validation writer that performs final verification after all sections have been processed.
 * Checks if there are any unverified gotoIds remaining.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationWriter implements ItemWriter<ValidationProcessor.ValidationData> {

    private final ValidationProcessor validationProcessor;

    @Override
    public void write(Chunk<? extends ValidationProcessor.ValidationData> chunk) {
        log.info("Step 1: Processing validation chunk of {} items", chunk.size());

        for (ValidationProcessor.ValidationData data : chunk) {
            if (data != null) {
                log.info("Processing validation data for book: '{}'", data.getBookTitle());
            }
        }
    }

    @AfterStep
    public void afterStep(StepExecution stepExecution) {
        log.info("Finalizing validation after step completion");

        // After processing the entire file, check if there are unverified gotoIds
        Set<Long> unverifiedGotoIds = validationProcessor.getUnverifiedGotoIds();
        Set<Long> sectionIds = validationProcessor.getSectionIds();

        if (!unverifiedGotoIds.isEmpty()) {
            String errorMessage = String.format(
                    "Invalid book JSON: Found %d gotoId(s) pointing to non-existent sections: %s. " +
                    "Valid section IDs are: %s",
                    unverifiedGotoIds.size(),
                    unverifiedGotoIds,
                    sectionIds);
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        log.info("✓ Validation successful! All {} gotoId references are valid.", sectionIds.size());
    }
}
