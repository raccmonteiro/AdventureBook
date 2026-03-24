package com.pictet.adventurebook.game.persistence.api;

import com.pictet.adventurebook.game.domain.Session;

import java.util.List;
import java.util.Optional;

/**
 * Repository for SavedGame (Session), long-term storage.
 * This is used to store the current state of a session for a long time.
 */
public interface SavedGameRepository {

    /**
     * Save (or create) a session to the database.
     * This is used to create a new Session, or to save sessions for players that want to keep their progress for a long time.
     * @param session
     * @return the saved session
     */
    Session save(Session session);

    /**
     * Retrieve a session by its id
     * @param id
     * @return
     */
    Optional<Session> findById(Long id);

}
