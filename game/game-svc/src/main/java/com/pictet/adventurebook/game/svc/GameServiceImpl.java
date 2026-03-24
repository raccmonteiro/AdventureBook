package com.pictet.adventurebook.game.svc;

import com.pictet.adventurebook.common.result.Result;
import com.pictet.adventurebook.game.GameService;
import com.pictet.adventurebook.game.domain.*;
import com.pictet.adventurebook.game.svc.features.chooseoption.ChooseOptionHandler;
import com.pictet.adventurebook.game.svc.features.SaveGameHandler;
import com.pictet.adventurebook.game.svc.features.StartGameHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final StartGameHandler startGameHandler;
    private final ChooseOptionHandler chooseOptionHandler;
    private final SaveGameHandler saveGameHandler;

    @Override
    public Result<SessionMaterialized> startGame(Long bookId) {
        return startGameHandler.handle(bookId);
    }

    @Override
    public Result<SessionMaterialized> chooseOption(Long sessionId, Long optionId) {

        //TODO if option is not valid, return the error and current section and state

        return chooseOptionHandler.handle(sessionId, optionId);
    }

    public Result<Boolean> saveGame(Long sessionId) {
        return saveGameHandler.handle(sessionId);
    }

}
