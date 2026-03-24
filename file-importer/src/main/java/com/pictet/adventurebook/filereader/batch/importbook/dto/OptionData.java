package com.pictet.adventurebook.filereader.batch.importbook.dto;


import com.pictet.adventurebook.game.persistence.entity.ConsequenceEntity;
import com.pictet.adventurebook.game.persistence.entity.OptionEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionData {
    private OptionEntity option;
    private ConsequenceEntity consequence;
}
