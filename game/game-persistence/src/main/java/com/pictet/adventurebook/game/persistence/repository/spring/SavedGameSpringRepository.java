package com.pictet.adventurebook.game.persistence.repository.spring;

import com.pictet.adventurebook.game.persistence.entity.SavedGameEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SavedGameSpringRepository extends CrudRepository<SavedGameEntity, Long> {
}
