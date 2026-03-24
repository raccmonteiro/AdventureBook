package com.pictet.adventurebook.filereader.model;

import lombok.Getter;

@Getter
public class ImportItem {
    private final BookHeader header;
    private final SectionJson section;

    public ImportItem(BookHeader header) {
        this.header = header;
        this.section = null;
    }

    public ImportItem(SectionJson section) {
        this.header = null;
        this.section = section;
    }

    public boolean isHeader() {
        return header != null;
    }

    public boolean isSection() {
        return section != null;
    }
}
