package com.pictet.adventurebook.game.persistence.api;

import com.pictet.adventurebook.game.domain.Section;
import com.pictet.adventurebook.game.domain.SectionWithOptions;

import java.util.Optional;

/**
 * Repository for Section
 */
public interface SectionRepository {

    /**
     * Retrieve a section by its id
     * @param sectionId
     * @return a section
     */
    Optional<Section> findById(Long sectionId);

    /**
     * Retrieve the first section of a book with its options
     * @param bookId
     * @return the first section of a book with its options
     */
    Optional<SectionWithOptions> findFirstSectionByBookIdWithOptions(Long bookId);

    /**
     * Retrieve a section with its options by its id
     * @param sectionId
     * @return a section with its options
     */
    Optional<SectionWithOptions> findByIdWithOptions(Long sectionId);
}
