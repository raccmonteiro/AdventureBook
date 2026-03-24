package com.pictet.adventurebook.filereader.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookHeader {
    private String title;
    private String author;
    private String difficulty;
    private long fileId;
}
