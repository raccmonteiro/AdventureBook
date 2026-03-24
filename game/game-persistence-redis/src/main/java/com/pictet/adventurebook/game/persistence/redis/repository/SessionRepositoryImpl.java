package com.pictet.adventurebook.game.persistence.redis.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventurebook.game.domain.GameStatus;
import com.pictet.adventurebook.game.domain.SessionState;
import com.pictet.adventurebook.game.domain.Session;
import com.pictet.adventurebook.game.domain.type.SectionId;
import com.pictet.adventurebook.game.domain.type.SessionId;
import com.pictet.adventurebook.game.persistence.api.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepository {

    // TODO memory DB should be moved outside of persistence module

    private static final String SESSION_KEY_PREFIX = "session:";
    private static final long SESSION_TTL_HOURS = 24;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Session save(Session session) {
        try {
            String key = SESSION_KEY_PREFIX + session.getId();
            String value = objectMapper.writeValueAsString(session);
            redisTemplate.opsForValue().set(key, value, SESSION_TTL_HOURS, TimeUnit.HOURS);
            log.debug("Saved session {} to Redis", session.getId());
            return session;
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize session {}", session.getId(), e);
            throw new RuntimeException("Failed to save session to Redis", e);
        }
    }

    @Override
    public Optional<Session> findById(Long id) {
        try {
            String key = SESSION_KEY_PREFIX + id;
            String value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                log.debug("Session {} not found in Redis", id);
                return Optional.empty();
            }
            Session session = deserializeSession(value);
            log.debug("Retrieved session {} from Redis", id);
            return Optional.of(session);
        } catch (Exception e) {
            log.error("Failed to retrieve session {}", id, e);
            return Optional.empty();
        }
    }

    private Session deserializeSession(String json) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(json);

        GameStatus status = Optional.ofNullable(node.get("status"))
                                    .map(n -> GameStatus.valueOf(n.asText()))
                                    .orElse(GameStatus.IN_PROGRESS);

        return Session.builder()
                      .id(SessionId.valueOf(node.get("id").asLong()))
                      .sectionId(SectionId.valueOf(node.get("sectionId").asLong()))
                      .state(SessionState.builder().health(node.get("state").get("health").asDouble()).build())
                      .status(status)
                      .build();
    }
}
