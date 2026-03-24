package com.pictet.adventurebook.game.persistence.repository.spring;

import com.pictet.adventurebook.game.persistence.entity.OptionEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OptionSpringRepository extends CrudRepository<OptionEntity, Long> {

    List<OptionEntity> findBySectionId(Long sectionId);


    @Query("""
        SELECT s.*, c.id as consequence_id, c.type as consequence_type, c.value as consequence_value, c.text as consequence_text
        FROM option o
        LEFT JOIN consequence c ON o.id = c.option_id
        WHERE o.id = :optionId
        """)
    Optional<OptionEntity> findByIdWithConsequences(@Param("optionId") Long optionId);
}
