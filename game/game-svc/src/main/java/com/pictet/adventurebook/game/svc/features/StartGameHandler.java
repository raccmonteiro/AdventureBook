package com.pictet.adventurebook.game.svc.features;

import java.util.List;
import java.util.Optional;

import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import com.pictet.adventurebook.game.domain.GameStatus;
import com.pictet.adventurebook.game.domain.SessionState;
import com.pictet.adventurebook.game.domain.SectionWithOptions;
import com.pictet.adventurebook.game.domain.Session;
import com.pictet.adventurebook.game.domain.SessionMaterialized;
import com.pictet.adventurebook.game.domain.type.SessionId;
import com.pictet.adventurebook.game.persistence.api.SavedGameRepository;
import com.pictet.adventurebook.game.persistence.api.SectionRepository;
import com.pictet.adventurebook.game.persistence.api.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartGameHandler {

    private static final double DEFAULT_HEALTH = 100.0;

    private final SectionRepository sectionRepository;
    private final SessionRepository sessionRepository;
    private final SavedGameRepository savedGameRepository;

    /**
     * Start a new game session from the beginning of the book
     */
    @Transactional
    public Result<SessionMaterialized> handle(Long bookId) {
        log.info("Starting book {}", bookId);

        // Validate book exists
        final Optional<SectionWithOptions> maybeBeginSection = sectionRepository.findFirstSectionByBookIdWithOptions(bookId);
        if (maybeBeginSection.isEmpty()) {
            return Result.failure(ErrorCode.NOT_FOUND, "Book not found");
        }

        // Get the BEGIN section
        final SectionWithOptions firstSection = maybeBeginSection.get();

        // Save to database first to get an ID
        final Session dbSession = savedGameRepository.save(
            Session.builder()
                   .sectionId(firstSection.getId())
                   .state(SessionState.builder().health(DEFAULT_HEALTH).build()) // TODO initial health should be defined by book
                   .status(GameStatus.IN_PROGRESS) // TODO initial health should be defined by book
                   .build()
        );

        final SessionId sessionId = dbSession.getId();

        // Create new session with initial health
        final SessionMaterialized session = SessionMaterialized.builder()
            .id(sessionId)
            .sectionId(firstSection.getId())
            .state(dbSession.getState())
            .sectionWithOptions(SectionWithOptions.builder()
                .id(firstSection.getId())
                .text(firstSection.getText())
                .type(firstSection.getType())
                .options(firstSection.getOptions())
                .build()
            )
            .status(GameStatus.IN_PROGRESS)
            .consequences(List.of())
            .build();

        // Save to Redis (temporary storage)
        sessionRepository.save(dbSession);

        log.info("Created session {} for book {}", session.getId(), bookId);

        return Result.success(session);
    }
}
