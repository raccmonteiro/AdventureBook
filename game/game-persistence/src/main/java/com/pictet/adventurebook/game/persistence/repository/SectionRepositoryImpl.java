package com.pictet.adventurebook.game.persistence.repository;

import java.util.Optional;

import com.pictet.adventurebook.game.domain.Option;
import com.pictet.adventurebook.game.domain.Section;
import com.pictet.adventurebook.game.domain.SectionType;
import com.pictet.adventurebook.game.domain.SectionWithOptions;
import com.pictet.adventurebook.game.domain.type.OptionDescription;
import com.pictet.adventurebook.game.domain.type.OptionId;
import com.pictet.adventurebook.game.domain.type.SectionId;
import com.pictet.adventurebook.game.domain.type.SectionText;
import com.pictet.adventurebook.game.persistence.api.SectionRepository;
import com.pictet.adventurebook.game.persistence.entity.SectionEntity;
import com.pictet.adventurebook.game.persistence.repository.spring.SectionSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SectionRepositoryImpl implements SectionRepository {

    private final SectionSpringRepository sectionSpringRepository;
    private final SectionWithOptionsRepository sectionWithOptionsRepository;

    @Override
    public Optional<Section> findById(Long sectionId) {
        return sectionSpringRepository.findById(sectionId)
                .map(this::toModel);
    }
    @Override
    public Optional<SectionWithOptions> findFirstSectionByBookIdWithOptions(Long bookId) {
        return sectionWithOptionsRepository.findFirstSectionByBookIdWithOptions(bookId)
                .map(this::toModelWithOptions);
    }

    @Override
    public Optional<SectionWithOptions> findByIdWithOptions(Long sectionId) {
        return sectionWithOptionsRepository.findByIdWithOptions(sectionId)
                .map(this::toModelWithOptions);
    }

    private Section toModel(SectionEntity entity) {
        return Section.builder()
                .id(SectionId.valueOf(entity.getId()))
                .text(SectionText.valueOf(entity.getText()))
                .type(SectionType.fromId(entity.getType()))
                .build();
    }

    private SectionWithOptions toModelWithOptions(SectionEntity entity) {
        return SectionWithOptions.builder()
                .id(SectionId.valueOf(entity.getId()))
                .text(SectionText.valueOf(entity.getText()))
                .type(SectionType.fromId(entity.getType()))
                .options(entity.getOptions().stream().map(optionEntity -> Option.builder()
                    .id(OptionId.valueOf(optionEntity.getId()))
                    .sectionId(SectionId.valueOf(optionEntity.getSectionId()))
                    .gotoId(SectionId.valueOf(optionEntity.getGotoId()))
                    .description(OptionDescription.valueOf(optionEntity.getDescription()))
                    .build()
                ).toList())
                .build();
    }
}
