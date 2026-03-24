package com.pictet.adventurebook.game.svc.features;

import java.util.Optional;

import com.pictet.adventurebook.common.result.AppError;
import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import com.pictet.adventurebook.game.domain.Session;
import com.pictet.adventurebook.game.persistence.api.SavedGameRepository;
import com.pictet.adventurebook.game.persistence.api.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveGameHandler {

    private final SessionRepository sessionRepository;
    private final SavedGameRepository savedGameRepository;

    /**
     * Save the current game session to persistent storage (PostgreSQL)
     */
    @Transactional
    public Result<Boolean> handle(Long sessionId) {
        log.info("Saving session {} to persistent storage", sessionId);

        // Retrieve session from Redis
        Optional<Session> maybeSession = sessionRepository.findById(sessionId);
        if (maybeSession.isEmpty()) {
            return Result.failure(ErrorCode.NOT_FOUND, "Session not found");
        }
        Session session = maybeSession.get();

        // Save to PostgreSQL
        savedGameRepository.save(session);

        log.info("Session {} saved to database", sessionId);

        return Result.success(true);
    }
}
