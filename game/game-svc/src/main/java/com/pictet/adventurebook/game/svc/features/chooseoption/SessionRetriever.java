package com.pictet.adventurebook.game.svc.features.chooseoption;

import java.util.Optional;

import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import com.pictet.adventurebook.game.domain.Session;
import com.pictet.adventurebook.game.persistence.api.SavedGameRepository;
import com.pictet.adventurebook.game.persistence.api.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionRetriever {

    private final SessionRepository sessionRepository;
    private final SavedGameRepository savedGameRepository;

    /**
     * Retrieve the session from Redis or PostgreSQL if not found in Redis.
     * This allows to recover sessions that were evicted from Redis, at the cost of a higher latency for the first request after eviction.
     */
    Result<Session> getSession(Long sessionId) {

        // Retrieve session from Redis
        Optional<Session> maybeSession = sessionRepository.findById(sessionId);
        if (maybeSession.isEmpty()) {

            // Try to recover from postgres
            maybeSession = savedGameRepository.findById(sessionId);
            if (maybeSession.isEmpty()) {
                return Result.failure(ErrorCode.NOT_FOUND, "Session not found");
            }

            // Save back to redis for future calls
            log.info("Recovered session {} from database", sessionId);
            sessionRepository.save(maybeSession.get());
        }
        Session session = maybeSession.get();

        if (! session.getState().isAlive()) {
            return Result.failure(ErrorCode.VALIDATION_ERROR, "Player is dead. Cannot choose chosenOption.");
        }

        return Result.success(session);
    }
}
