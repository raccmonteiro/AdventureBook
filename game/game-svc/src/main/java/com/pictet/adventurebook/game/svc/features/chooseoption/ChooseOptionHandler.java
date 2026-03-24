package com.pictet.adventurebook.game.svc.features.chooseoption;

import java.util.List;

import com.pictet.adventurebook.common.result.Result;
import com.pictet.adventurebook.game.domain.*;
import com.pictet.adventurebook.game.persistence.api.SectionRepository;
import com.pictet.adventurebook.game.persistence.api.SessionRepository;
import com.pictet.adventurebook.game.svc.mechanics.GameMechanicsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChooseOptionHandler {

    private final SessionRetriever sessionRetriever;
    private final OptionRetriever optionRetriever;
    private final SectionRepository sectionRepository;
    private final SessionRepository sessionRepository;
    private final GameMechanicsService gameMechanicsService;

    /**
     * Choose an option and jump to the next section, applying consequences
     */
    @Transactional
    public Result<SessionMaterialized> handle(Long sessionId, Long optionId) {
        log.info("Session {} choosing chosenOption {}", sessionId, optionId);

        final Result<Session> sessionResult = sessionRetriever.getSession(sessionId);
        if (sessionResult.isFailure()) {
            return Result.failure(sessionResult.getError());
        }

        final Result<OptionWithConsequences> optionResult =
            optionRetriever.getOption(optionId, sessionResult.getValue().getSectionId());
        if (optionResult.isFailure()) {
            return Result.failure(optionResult.getError());
        }

        final Session session = sessionResult.getValue();
        final OptionWithConsequences chosenOption = optionResult.getValue();
        final SectionWithOptions nextSection = getNextSection(chosenOption);

        gameMechanicsService.applyConsequences(session, chosenOption.getConsequences());

        SessionMaterialized updatedSessionMaterialized;
        if (session.getState().isAlive()) {
            log.info("Session {} moved to section {}", sessionId, nextSection.getId());
            session.setSectionId(nextSection.getId()); // Update session with new section
            session.setStatus(GameStatus.IN_PROGRESS); // Ensure status is set
            
            // Check if next section is END
            if (nextSection.getType() == SectionType.END) {
                session.setStatus(GameStatus.WON);
            }
            
            updatedSessionMaterialized = getSessionMaterialized(session, nextSection, chosenOption);
        }
        else {
            log.info("Player in session {} has died", sessionId);
            session.setStatus(GameStatus.LOST); // Update session status
            updatedSessionMaterialized = getSessionForLosingGame(session, chosenOption);
        }

        // Save updated session to Redis
        sessionRepository.save(session);

        return Result.success(updatedSessionMaterialized);
    }

    private @NonNull SectionWithOptions getNextSection(OptionWithConsequences chosenOption) {
        return sectionRepository.findByIdWithOptions(chosenOption.getGotoId().getValue())
                                .orElseThrow(  // this should never happen, as the option should have been validated when created
                () -> new IllegalStateException("Section not found: " + chosenOption.getGotoId().getValue())
            );
    }

    private static SessionMaterialized getSessionMaterialized(
        final Session session,
        final SectionWithOptions nextSection,
        final OptionWithConsequences option
    ) {
        // Update session with new section and state
        return SessionMaterialized.builder()
            .id(session.getId())
            .sectionId(nextSection.getId())
            .state(session.getState())
            .sectionWithOptions(SectionWithOptions.builder()
                .id(nextSection.getId())
                .text(nextSection.getText())
                .type(nextSection.getType())
                .options(nextSection.getOptions())
                .build()
            )
            .status(session.getStatus())
            .consequences(option.getConsequences())
            .build()
            ;
    }

    private SessionMaterialized getSessionForLosingGame(
        final Session session,
        final OptionWithConsequences chosenOption
    ) {

        // Don't show next currentSection if player is dead, we want to show the last currentSection with the consequences
        Section currentSection = sectionRepository
            .findById(chosenOption.getSectionId().getValue())
            .orElseThrow(
                () -> new IllegalStateException("Section not found: " + chosenOption.getSectionId().getValue())  // this should never happen
            );

        return SessionMaterialized.builder()
            .id(session.getId())
            .sectionId(currentSection.getId())
            .state(session.getState())
            .sectionWithOptions(SectionWithOptions.builder()
                .id(currentSection.getId())
                .text(currentSection.getText())
                .type(currentSection.getType())
                .options(List.of()) // no options, player is dead
                .build()
            )
            .status(GameStatus.LOST)
            .consequences(chosenOption.getConsequences())
            .build();
    }
}
