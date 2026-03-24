package com.pictet.adventurebook.game.persistence.api;

import java.util.List;

import com.pictet.adventurebook.game.domain.Consequence;

/**
 * Repository for Consequence
 */
public interface ConsequenceRepository {


    /**
     * Retrieve all consequences for a given option
     * @param optionId the id of the option
     * @return a list of consequences
     */
    List<Consequence> findByOption(Long optionId);

}
