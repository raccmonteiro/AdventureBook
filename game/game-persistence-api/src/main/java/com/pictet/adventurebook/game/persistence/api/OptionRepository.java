package com.pictet.adventurebook.game.persistence.api;

import java.util.List;
import java.util.Optional;

import com.pictet.adventurebook.game.domain.Option;
import com.pictet.adventurebook.game.domain.OptionWithConsequences;

/**
 * Repository for Option
 */
public interface OptionRepository {


    /**
     * Retrieve all options for a given section
     * @param sectionId
     * @return a list of options
     */
    List<Option> findBySection(Long sectionId);

    /**
     * Retrieve an option with its consequences
     * @param optionId
     * @return an option with its consequences
     */
    Optional<OptionWithConsequences> findByIdWithConsequences(Long optionId);

}
