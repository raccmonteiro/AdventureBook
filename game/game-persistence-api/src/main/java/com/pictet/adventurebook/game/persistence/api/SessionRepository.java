package com.pictet.adventurebook.game.persistence.api;

import com.pictet.adventurebook.game.domain.Session;
import com.pictet.adventurebook.game.domain.SessionMaterialized;

import java.util.Optional;

/**
 * Repository for Session, short-term storage.
 * This is used to store the current state of a session while the player is playing, and to retrieve it when the player wants to continue playing.
 */
public interface SessionRepository {

    /**
     * Save a session to database.
     * @param session
     * @return the saved session
     */
    Session save(Session session);

    /**
     * Retrieve a session by its id
     * @param id
     * @return the session
     */
    Optional<Session> findById(Long id);

}
