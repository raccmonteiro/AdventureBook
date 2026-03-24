package com.pictet.adventurebook.game.web;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import com.pictet.adventurebook.game.domain.Consequence;
import com.pictet.adventurebook.game.domain.Option;
import com.pictet.adventurebook.game.domain.SessionState;
import com.pictet.adventurebook.game.domain.SectionWithOptions;
import com.pictet.adventurebook.game.domain.SessionMaterialized;
import com.pictet.adventurebook.game.model.ConsequenceDto;
import com.pictet.adventurebook.game.model.GameSessionResponse;
import com.pictet.adventurebook.game.model.OptionDto;
import com.pictet.adventurebook.game.model.PlayerStatsDto;
import com.pictet.adventurebook.game.model.SectionDto;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    GameSessionResponse toApiSessionResponse(SessionMaterialized session) {
        GameSessionResponse response = new GameSessionResponse();
        response.setSessionId(session.getId().getValue());
        response.setCurrentSection(toApiSectionDto(session.getSectionWithOptions()));
        response.setPlayerStats(toApiPlayerStatsDto(session.getState()));
        response.setConsequences(session.getConsequences().stream()
                                        .map(this::toApiConsequenceDto)
                                        .collect(Collectors.toList()));
        response.setStatus(GameSessionResponse.StatusEnum.fromValue(session.getStatus().name()));
        return response;
    }

    private ConsequenceDto toApiConsequenceDto(Consequence consequence) {
        ConsequenceDto consequenceDto = new ConsequenceDto();
        consequenceDto.setType(consequence.getType().getValue());
        consequenceDto.setValue(BigDecimal.valueOf(consequence.getValue().getValue()));
        consequenceDto.setText(consequence.getText().getValue());
        return consequenceDto;
    }

    private PlayerStatsDto toApiPlayerStatsDto(SessionState stats) {
        PlayerStatsDto playerStatsDto = new PlayerStatsDto();
        playerStatsDto.setHealth(stats.getHealth());
        return playerStatsDto;
    }

    private SectionDto toApiSectionDto(SectionWithOptions section) {
        SectionDto sectionDto = new SectionDto();
        sectionDto.setId(section.getId().getValue());
        sectionDto.setText(section.getText().getValue());
        sectionDto.setOptions(section.getOptions().stream()
                                     .map(this::toApiOptionDto)
                                     .collect(Collectors.toList()));
        return sectionDto;
    }

    private OptionDto toApiOptionDto(Option option) {
        OptionDto optionDto = new OptionDto();
        optionDto.setId(option.getId().getValue());
        optionDto.setDescription(option.getDescription().getValue());
        return optionDto;
    }

}
