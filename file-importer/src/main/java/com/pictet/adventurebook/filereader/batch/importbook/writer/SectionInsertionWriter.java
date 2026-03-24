package com.pictet.adventurebook.filereader.batch.importbook.writer;

import com.pictet.adventurebook.filereader.batch.BookImportProcessor;
import com.pictet.adventurebook.filereader.batch.importbook.dto.BookImportData;
import com.pictet.adventurebook.filereader.batch.importbook.dto.SectionData;
import com.pictet.adventurebook.game.persistence.entity.SectionEntity;
import com.pictet.adventurebook.game.persistence.repository.spring.SectionSpringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * Step 3: Inserts sections into database without options/consequences
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SectionInsertionWriter implements ItemWriter<BookImportData> {

    private final SectionSpringRepository sectionSpringRepository;
    private final BookImportProcessor processor;

    @Override
    @Transactional
    public void write(Chunk<? extends BookImportData> chunk) {
        log.info("Step 3: Writing sections for chunk of {} items to the database", chunk.size());
        try {
            Long bookId = processor.getCurrentBookId();
            if (bookId == null) {
                throw new IllegalStateException("BookId is not set. Book insertion step must run before section insertion.");
            }

            for (BookImportData data : chunk) {
                if (data.getSections() != null && !data.getSections().isEmpty()) {
                    // Save all Sections in this data object (without options)
                    for (SectionData sd : data.getSections()) {
                        SectionEntity section = sd.getSection();
                        Long oldId = sd.getJsonId(); // Use the stored JSON ID
                        section.setId(null); // Clear ID to let DB generate a new one
                        section.setBookId(bookId);
                        section.setOptions(new HashSet<>()); // Ensure options are not saved in this step
                        SectionEntity savedSection = sectionSpringRepository.save(section);

                        // Store mapping for use in step 4
                        processor.getSectionIdMap().put(oldId, savedSection.getId());
                        log.debug("Mapped JSON section ID {} to DB section ID {}", oldId, savedSection.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error writing sections to database", e);
            throw e;
        }
    }
}
