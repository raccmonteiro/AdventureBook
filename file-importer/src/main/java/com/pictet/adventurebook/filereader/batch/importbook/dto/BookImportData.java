package com.pictet.adventurebook.filereader.batch.importbook.dto;

import java.util.List;

import com.pictet.adventurebook.book.persistence.entity.BookEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookImportData {
    private BookEntity book;
    private List<SectionData> sections;

}

