package com.pictet.adventurebook.filereader.batch.importbook.writer;

import com.pictet.adventurebook.filereader.batch.BookImportProcessor;
import com.pictet.adventurebook.filereader.batch.importbook.dto.BookImportData;
import com.pictet.adventurebook.filereader.batch.importbook.dto.OptionData;
import com.pictet.adventurebook.filereader.batch.importbook.dto.SectionData;
import com.pictet.adventurebook.game.persistence.entity.ConsequenceEntity;
import com.pictet.adventurebook.game.persistence.entity.OptionEntity;
import com.pictet.adventurebook.game.persistence.repository.spring.SectionSpringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Step 3: Inserts options and consequences into database
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OptionsInsertionWriter implements ItemWriter<BookImportData> {

    private final SectionSpringRepository sectionSpringRepository;
    private final BookImportProcessor processor;

    @Override
    @Transactional
    public void write(Chunk<? extends BookImportData> chunk) {
        log.info("Step 3: Writing options and consequences for chunk of {} items to the database", chunk.size());
        try {
            Map<Long, Long> sectionMapping = processor.getSectionIdMap();

            for (BookImportData data : chunk) {
                if (data.getSections() == null || data.getSections().isEmpty()) {
                    continue;
                }

                int optionsSaved = 0;
                int consequencesSaved = 0;

                for (SectionData sd : data.getSections()) {
                    if (sd.getOptions() != null && !sd.getOptions().isEmpty()) {
                        Long newSectionId = sectionMapping.get(sd.getJsonId());

                        if (newSectionId == null) {
                            log.error("CRITICAL ERROR: JSON section ID {} not found in mapping. This should not happen.", sd.getJsonId());
                            throw new IllegalStateException("JSON section ID " + sd.getJsonId() + " not found in mapping");
                        }

                        Set<OptionEntity> optionsToSave = new HashSet<>();

                        for (OptionData od : sd.getOptions()) {
                            OptionEntity option = od.getOption();

                            Long newGotoId = sectionMapping.get(option.getGotoId());
                            if (newGotoId == null) {
                                log.error("CRITICAL ERROR: Target JSON section ID {} not found in mapping for section DB ID {}. This should not happen if validation passed.", option.getGotoId(), newSectionId);
                                throw new IllegalStateException("Target section ID " + option.getGotoId() + " not found in mapping");
                            }

                            Set<ConsequenceEntity> consequences = new HashSet<>();
                            if (od.getConsequence() != null) {
                                ConsequenceEntity consequence = od.getConsequence();
                                consequences.add(ConsequenceEntity.builder()
                                        .type(consequence.getType())
                                        .value(consequence.getValue())
                                        .text(consequence.getText())
                                        .build());
                                consequencesSaved++;
                            }

                            optionsToSave.add(OptionEntity.builder()
                                    .sectionId(newSectionId)
                                    .gotoId(newGotoId)
                                    .description(option.getDescription())
                                    .consequences(consequences)
                                    .build());
                            optionsSaved++;
                        }

                        // Reload section and update with options
                        sectionSpringRepository.findById(newSectionId).ifPresent(section -> {
                            section.setOptions(optionsToSave);
                            sectionSpringRepository.save(section);
                        });
                    }
                }
                if (optionsSaved > 0) {
                   log.debug("Saved {} options and {} consequences.", optionsSaved, consequencesSaved);
                }
            }
        } catch (Exception e) {
            log.error("Error writing options and consequences to database", e);
            throw e;
        }
    }
}
