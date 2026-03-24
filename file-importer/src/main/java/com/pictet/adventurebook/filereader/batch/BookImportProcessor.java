package com.pictet.adventurebook.filereader.batch;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pictet.adventurebook.book.domain.type.BookStatus;
import com.pictet.adventurebook.book.domain.type.Difficulty;
import com.pictet.adventurebook.book.persistence.entity.BookEntity;
import com.pictet.adventurebook.filereader.batch.importbook.dto.BookImportData;
import com.pictet.adventurebook.filereader.batch.importbook.dto.OptionData;
import com.pictet.adventurebook.filereader.batch.importbook.dto.SectionData;
import com.pictet.adventurebook.filereader.model.BookHeader;
import com.pictet.adventurebook.filereader.model.ConsequenceJson;
import com.pictet.adventurebook.filereader.model.ImportItem;
import com.pictet.adventurebook.filereader.model.OptionJson;
import com.pictet.adventurebook.filereader.model.SectionJson;
import com.pictet.adventurebook.game.domain.SectionType;
import com.pictet.adventurebook.game.persistence.entity.ConsequenceEntity;
import com.pictet.adventurebook.game.persistence.entity.OptionEntity;
import com.pictet.adventurebook.game.persistence.entity.SectionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.ItemProcessor;

import java.util.Collections;

@Slf4j
public class BookImportProcessor implements ItemProcessor<ImportItem, BookImportData> {

    private Long currentBookId;
    private Long currentFileId;
    private final Map<Long, Long> sectionIdMap = new ConcurrentHashMap<>();


    public void setCurrentBookId(Long currentBookId) {
        this.currentBookId = currentBookId;
    }

    public Long getCurrentBookId() {
        return currentBookId;
    }

    public void setCurrentFileId(Long currentFileId) {
        this.currentFileId = currentFileId;
    }

    public Long getCurrentFileId() {
        return currentFileId;
    }

    public Map<Long, Long> getSectionIdMap() {
        return sectionIdMap;
    }

    @Override
    public BookImportData process(ImportItem item) {
        if (item.isHeader()) {
            BookHeader header = item.getHeader();
            log.info("Processing book header: {}", header.getTitle());

            BookEntity book = BookEntity.builder()
                    .title(header.getTitle())
                    .author(header.getAuthor())
                    .difficultyId(Difficulty.valueOf(header.getDifficulty().toUpperCase()).getId())
                    .status(BookStatus.INACTIVE.getId())
                    .build();

            return BookImportData.builder()
                    .book(book)
                    .sections(Collections.emptyList())
                    .build();
        }

        if (item.isSection()) {
            SectionJson sj = item.getSection();
            log.info("Processing section: {}", sj.getId());

            SectionEntity section = SectionEntity.builder()
                    .id(sj.getId())
                    .type(SectionType.valueOf(sj.getType().toUpperCase()).getId())
                    .text(sj.getText())
                    .build();

            List<OptionData> options = new java.util.ArrayList<>();
            if (sj.getOptions() != null) {
                for (OptionJson oj : sj.getOptions()) {
                    OptionEntity option = OptionEntity.builder()
                            .sectionId(sj.getId())
                            .gotoId(oj.getGotoId())
                            .description(oj.getDescription())
                            .build();

                    ConsequenceEntity consequence = null;
                    if (oj.getConsequence() != null) {
                        ConsequenceJson cj = oj.getConsequence();
                        consequence = ConsequenceEntity.builder()
                                .type(cj.getType())
                                .value(Double.valueOf(cj.getValue()))
                                .text(cj.getText())
                                .build();
                    }

                    options.add(OptionData.builder()
                            .option(option)
                            .consequence(consequence)
                            .build());
                }
            }

            SectionData sectionData = SectionData.builder()
                    .section(section)
                    .options(options)
                    .jsonId(sj.getId())
                    .build();

            return BookImportData.builder()
                    .book(null)
                    .sections(Collections.singletonList(sectionData))
                    .build();
        }

        return null;
    }
}
