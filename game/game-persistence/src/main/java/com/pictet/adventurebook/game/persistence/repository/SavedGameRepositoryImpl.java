package com.pictet.adventurebook.game.persistence.repository;

import java.time.Instant;
import java.util.Optional;

import com.pictet.adventurebook.common.domain.Value;
import com.pictet.adventurebook.game.domain.GameStatus;
import com.pictet.adventurebook.game.domain.Session;
import com.pictet.adventurebook.game.domain.type.SectionId;
import com.pictet.adventurebook.game.domain.type.SessionId;
import com.pictet.adventurebook.game.persistence.api.SavedGameRepository;
import com.pictet.adventurebook.game.persistence.entity.SavedGameEntity;
import com.pictet.adventurebook.game.persistence.repository.spring.SavedGameSpringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SavedGameRepositoryImpl implements SavedGameRepository {

    private final SavedGameSpringRepository springRepository;

    @Override
    public Session save(Session session) {
        Instant now = Instant.now();
        SavedGameEntity entity = SavedGameEntity.builder()
                .id(  // null for new sessions, DB will auto-generate
                    Optional.ofNullable(session.getId())
                            .map(Value::getValue)
                            .orElse(null)
                )
                .sectionId(session.getSectionId().getValue())
                .state(session.getState())
                .status(session.getStatus().getId())
                .createdAt(now)
                .updatedAt(now)
                .build();

        SavedGameEntity saved = springRepository.save(entity);
        log.debug("Saved game session (id in DB: {}) to database", saved.getId());

        return toSession(saved, saved.getId());
    }

    @Override
    public Optional<Session> findById(Long id) {
        return springRepository.findById(id)
                .map(entity -> this.toSession(entity, id));
    }

    private Session toSession(SavedGameEntity entity, Long sessionId) {
        return Session.builder()
                .id(SessionId.valueOf(sessionId))
                .sectionId(SectionId.valueOf(entity.getSectionId()))
                .state(entity.getState())
                .status(GameStatus.fromId(entity.getStatus()))
                .build();
    }
}
