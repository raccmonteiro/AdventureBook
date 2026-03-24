package com.pictet.adventurebook.filereader.model;

import java.util.List;
import lombok.Data;

@Data
public class SectionJson {
    private Long id;
    private String text;
    private String type;
    private List<OptionJson> options;
}
