package com.pictet.adventurebook.game.persistence.repository.spring;

import com.pictet.adventurebook.game.persistence.entity.SectionEntity;
import org.springframework.data.repository.CrudRepository;

public interface SectionSpringRepository extends CrudRepository<SectionEntity, Long> {

}
