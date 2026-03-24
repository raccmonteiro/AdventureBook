package com.pictet.adventurebook.game;

import com.pictet.adventurebook.common.result.Result;
import com.pictet.adventurebook.game.domain.Session;
import com.pictet.adventurebook.game.domain.SessionMaterialized;

/**
 * Service for game operations.
 * This is the main entry point for the game service, and is used by the API layer to handle incoming requests.
 * It orchestrates the different features of the game, such as starting a new game, choosing an option, and saving the game.
 */
public interface GameService {

    /**
     * Start a new game session for a given book.
     * This will create a new session in the database and return the initial state of the game.
     * @param bookId the id of the book to start a new game with
     * @return the initial state of the game session
     */
    Result<SessionMaterialized> startGame(Long bookId);

    /**
     * Choose an option for a given session.
     * This will update the session state based on the consequences of the chosen option, and return the new state of the game.
     * @param sessionId the id of the session to update
     * @param optionId the id of the option to choose
     * @return the new state of the game session after applying the consequences of the chosen option
     */
    Result<SessionMaterialized> chooseOption(Long sessionId, Long optionId);

    /** Save the current game session to persistent storage (PostgreSQL).
     * This will retrieve the current session state from Redis and save it to the database, allowing the player to continue playing later.
     * @param sessionId the id of the session to save
     * @return true if the game was saved successfully, false otherwise
     */
    Result<Boolean> saveGame(Long sessionId);
}
