package com.pictet.adventurebook.game.persistence.repository;

import com.pictet.adventurebook.game.domain.Consequence;
import com.pictet.adventurebook.game.domain.type.ConsequenceId;
import com.pictet.adventurebook.game.domain.type.ConsequenceText;
import com.pictet.adventurebook.game.domain.type.ConsequenceType;
import com.pictet.adventurebook.game.domain.type.ConsequenceValue;
import com.pictet.adventurebook.game.persistence.api.ConsequenceRepository;
import com.pictet.adventurebook.game.persistence.entity.ConsequenceEntity;
import com.pictet.adventurebook.game.persistence.repository.spring.ConsequenceSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConsequenceRepositoryImpl implements ConsequenceRepository {

    private final ConsequenceSpringRepository consequenceSpringRepository;

    @Override
    public List<Consequence> findByOption(Long optionId) {
        return consequenceSpringRepository.findByOptionId(optionId).stream()
                .map(this::toModel)
                .toList();
    }

    private Consequence toModel(ConsequenceEntity entity) {
        return Consequence.builder()
                .id(ConsequenceId.valueOf(entity.getId()))
                .type(ConsequenceType.valueOf(entity.getType()))
                .value(ConsequenceValue.valueOf(entity.getValue()))
                .text(ConsequenceText.valueOf(entity.getText()))
                .build();
    }
}
