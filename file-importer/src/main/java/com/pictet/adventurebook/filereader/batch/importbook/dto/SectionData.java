package com.pictet.adventurebook.filereader.batch.importbook.dto;


import java.util.List;

import com.pictet.adventurebook.game.persistence.entity.SectionEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class SectionData {
    private SectionEntity section;
    private List<OptionData> options;
    private Long jsonId;
}
