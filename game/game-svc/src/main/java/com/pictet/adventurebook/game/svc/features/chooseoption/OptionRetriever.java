package com.pictet.adventurebook.game.svc.features.chooseoption;

import java.util.Optional;

import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import com.pictet.adventurebook.game.domain.OptionWithConsequences;
import com.pictet.adventurebook.game.domain.type.SectionId;
import com.pictet.adventurebook.game.persistence.api.OptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OptionRetriever {

    private final OptionRepository optionRepository;


    /**
     * Get an option from the database and validate that it belongs to the current section
     * TODO: Consider adding a cache layer here
     */
    Result<OptionWithConsequences> getOption(final Long optionId, final SectionId currentSectionId) {
        // Get current section
        Optional<OptionWithConsequences> maybeOption = optionRepository.findByIdWithConsequences(optionId);
        if (maybeOption.isEmpty()) {
            return Result.failure(ErrorCode.INVALID_INPUT, "Option not found");
        }
        OptionWithConsequences chosenOption = maybeOption.get();
        if (! chosenOption.getSectionId().equals(currentSectionId)) {
            return Result.failure(ErrorCode.VALIDATION_ERROR, "Option does not belong to current section");
        }

        return Result.success(chosenOption);
    }
}
