package com.pictet.adventurebook.filereader.batch.importbook;

import com.pictet.adventurebook.filereader.model.ImportItem;
import com.pictet.adventurebook.filereader.model.OptionJson;
import com.pictet.adventurebook.filereader.model.SectionJson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.ItemProcessor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Memory-efficient validation processor.
 * Only keeps track of section IDs and gotoIds that need verification.
 */
@Slf4j
public class ValidationProcessor implements ItemProcessor<ImportItem, ValidationProcessor.ValidationData> {

    private final Set<Long> sectionIds = ConcurrentHashMap.newKeySet();
    private final Set<Long> unverifiedGotoIds = ConcurrentHashMap.newKeySet();

    @Getter
    public static class ValidationData {
        private final String bookTitle;

        public ValidationData(String bookTitle) {
            this.bookTitle = bookTitle;
        }
    }

    public Set<Long> getSectionIds() {
        return sectionIds;
    }

    public Set<Long> getUnverifiedGotoIds() {
        return unverifiedGotoIds;
    }

    @Override
    public ValidationData process(ImportItem item) {
        if (item.isHeader()) {
            log.info("Validating book structure: {}", item.getHeader().getTitle());
            return new ValidationData(item.getHeader().getTitle());
        }

        if (item.isSection()) {
            SectionJson section = item.getSection();
            Long sectionId = section.getId();

            if (sectionId == null) {
                throw new IllegalArgumentException("Invalid book JSON: section has null ID");
            }

            // Add section ID to our set
            sectionIds.add(sectionId);
            log.debug("Registered section ID: {}", sectionId);

            // Remove from unverified set if it was referenced before
            if (unverifiedGotoIds.remove(sectionId)) {
                log.debug("Section ID {} was previously referenced and is now verified", sectionId);
            }

            // Process options
            if (section.getOptions() != null) {
                for (OptionJson option : section.getOptions()) {
                    Long gotoId = option.getGotoId();

                    if (gotoId != null) {
                        // If gotoId already exists in sectionIds, it's verified
                        if (sectionIds.contains(gotoId)) {
                            log.debug("gotoId {} is already verified (points to earlier section)", gotoId);
                        } else {
                            // Add to unverified set (will be verified later)
                            unverifiedGotoIds.add(gotoId);
                            log.debug("gotoId {} added to unverified set (points to later section)", gotoId);
                        }
                    }
                }
            }
        }

        return null;
    }
}
