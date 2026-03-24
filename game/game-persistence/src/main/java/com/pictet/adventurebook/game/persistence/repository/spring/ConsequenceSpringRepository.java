package com.pictet.adventurebook.game.persistence.repository.spring;

import com.pictet.adventurebook.game.persistence.entity.ConsequenceEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConsequenceSpringRepository extends CrudRepository<ConsequenceEntity, Long> {

    List<ConsequenceEntity> findByOptionId(Long optionId);
}
