package com.pictet.adventurebook.game.persistence.repository;

import com.pictet.adventurebook.game.domain.Consequence;
import com.pictet.adventurebook.game.domain.Option;
import com.pictet.adventurebook.game.domain.OptionWithConsequences;
import com.pictet.adventurebook.game.domain.type.ConsequenceId;
import com.pictet.adventurebook.game.domain.type.ConsequenceText;
import com.pictet.adventurebook.game.domain.type.ConsequenceType;
import com.pictet.adventurebook.game.domain.type.ConsequenceValue;
import com.pictet.adventurebook.game.domain.type.OptionDescription;
import com.pictet.adventurebook.game.domain.type.OptionId;
import com.pictet.adventurebook.game.domain.type.SectionId;
import com.pictet.adventurebook.game.persistence.api.ConsequenceRepository;
import com.pictet.adventurebook.game.persistence.api.OptionRepository;
import com.pictet.adventurebook.game.persistence.entity.OptionEntity;
import com.pictet.adventurebook.game.persistence.repository.spring.OptionSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OptionRepositoryImpl implements OptionRepository {

    private final OptionSpringRepository optionSpringRepository;
    private final ConsequenceRepository consequenceRepository;

    @Override
    public List<Option> findBySection(Long sectionId) {
        return optionSpringRepository.findBySectionId(sectionId).stream()
                .map(this::toModel)
                .toList();
    }

    @Override
    public Optional<OptionWithConsequences> findByIdWithConsequences(Long optionId) {
        return optionSpringRepository.findById(optionId)
                .map(this::toModelWithConsequences);
    }

    private Option toModel(OptionEntity entity) {
//        List<Consequence> consequences = consequenceRepository.findByOption(entity.getId());

        return Option.builder()
            .id(OptionId.valueOf(entity.getId()))
            .sectionId(SectionId.valueOf(entity.getSectionId()))
            .gotoId(SectionId.valueOf(entity.getGotoId()))
            .description(OptionDescription.valueOf(entity.getDescription()))
            .build();
    }

    private OptionWithConsequences toModelWithConsequences(OptionEntity entity) {
        return OptionWithConsequences.builder()
            .id(OptionId.valueOf(entity.getId()))
            .sectionId(SectionId.valueOf(entity.getSectionId()))
            .gotoId(SectionId.valueOf(entity.getGotoId()))
            .description(OptionDescription.valueOf(entity.getDescription()))
            .consequences(entity.getConsequences().stream().map( consequenceEntity -> Consequence.builder()
                .id(ConsequenceId.valueOf(consequenceEntity.getId()))
                .type(ConsequenceType.valueOf(consequenceEntity.getType()))
                .value(ConsequenceValue.valueOf(consequenceEntity.getValue()))
                .text(ConsequenceText.valueOf(consequenceEntity.getText()))
                .build()
                ).toList()
            ).build();
    }
}
