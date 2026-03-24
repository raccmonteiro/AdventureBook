package com.pictet.adventurebook.game.web;

import com.pictet.adventurebook.common.result.Result;
import com.pictet.adventurebook.game.GameService;
import com.pictet.adventurebook.game.api.GameApi;
import com.pictet.adventurebook.game.domain.SessionMaterialized;
import com.pictet.adventurebook.game.model.ChooseOptionRequest;
import com.pictet.adventurebook.game.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class GameSessionsController implements GameApi {

    private final GameService gameService;
    private final SessionMapper sessionMapper;

    @Override
    public ResponseEntity<GameSessionResponse> startGame(StartGameRequest startGameRequest) {
        log.info("Starting book {}", startGameRequest.getBookId());
        Result<SessionMaterialized> result = gameService.startGame(startGameRequest.getBookId());

        return result.fold(
            session -> ResponseEntity.ok(sessionMapper.toApiSessionResponse(session)),
            error -> ResponseEntity.notFound().build()
        );

    }

    @Override
    public ResponseEntity<GameSessionResponse> chooseOption(Long sessionId, ChooseOptionRequest chooseOptionRequest) {
        log.info("Session {} choosing option {}", sessionId, chooseOptionRequest.getOptionId());
        Result<SessionMaterialized> result = gameService.chooseOption(sessionId, chooseOptionRequest.getOptionId());

        return result.fold(
            session -> ResponseEntity.ok(sessionMapper.toApiSessionResponse(session)),
            error -> ResponseEntity.notFound().build()
        );
    }

    @Override
    public ResponseEntity<Void> saveGame(Long sessionId) {
        log.info("Saving game session {}", sessionId);
        Result<Boolean> result = gameService.saveGame(sessionId);
        return result.fold(
            isSaved -> ResponseEntity.ok().build(),
            error -> ResponseEntity.notFound().build()
        );
    }

}
