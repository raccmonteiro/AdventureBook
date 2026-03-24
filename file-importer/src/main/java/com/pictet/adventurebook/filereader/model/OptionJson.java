package com.pictet.adventurebook.filereader.model;

import lombok.Data;

@Data
public class OptionJson {
    private String description;
    private Long gotoId;
    private ConsequenceJson consequence;
}
