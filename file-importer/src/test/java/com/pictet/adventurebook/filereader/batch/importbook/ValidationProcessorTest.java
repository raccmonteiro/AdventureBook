package com.pictet.adventurebook.filereader.batch.importbook;

import com.pictet.adventurebook.filereader.model.BookHeader;
import com.pictet.adventurebook.filereader.model.ImportItem;
import com.pictet.adventurebook.filereader.model.OptionJson;
import com.pictet.adventurebook.filereader.model.SectionJson;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationProcessorTest {

    private final ValidationProcessor processor = new ValidationProcessor();

    @Test
    void shouldRegisterSectionId() {
        SectionJson section = new SectionJson();
        section.setId(1L);
        ImportItem item = new ImportItem(section);

        processor.process(item);

        assertTrue(processor.getSectionIds().contains(1L));
        assertTrue(processor.getUnverifiedGotoIds().isEmpty());
    }

    @Test
    void shouldRegisterUnverifiedGotoId() {
        SectionJson section = new SectionJson();
        section.setId(1L);
        OptionJson option = new OptionJson();
        option.setGotoId(2L);
        section.setOptions(List.of(option));
        ImportItem item = new ImportItem(section);

        processor.process(item);

        assertTrue(processor.getSectionIds().contains(1L));
        assertTrue(processor.getUnverifiedGotoIds().contains(2L));
    }

    @Test
    void shouldVerifyGotoIdWhenSectionIsProcessedLater() {
        // Section 1 points to Section 2 (forward reference)
        SectionJson section1 = new SectionJson();
        section1.setId(1L);
        OptionJson option = new OptionJson();
        option.setGotoId(2L);
        section1.setOptions(List.of(option));
        processor.process(new ImportItem(section1));

        assertTrue(processor.getUnverifiedGotoIds().contains(2L));

        // Now process Section 2
        SectionJson section2 = new SectionJson();
        section2.setId(2L);
        processor.process(new ImportItem(section2));

        assertTrue(processor.getSectionIds().contains(2L));
        assertFalse(processor.getUnverifiedGotoIds().contains(2L));
    }

    @Test
    void shouldVerifyGotoIdWhenSectionIsProcessedEarlier() {
        // Process Section 1 first
        SectionJson section1 = new SectionJson();
        section1.setId(1L);
        processor.process(new ImportItem(section1));

        // Now Section 2 points back to Section 1 (backward reference)
        SectionJson section2 = new SectionJson();
        section2.setId(2L);
        OptionJson option = new OptionJson();
        option.setGotoId(1L);
        section2.setOptions(List.of(option));
        processor.process(new ImportItem(section2));

        assertTrue(processor.getSectionIds().contains(1L));
        assertTrue(processor.getSectionIds().contains(2L));
        assertTrue(processor.getUnverifiedGotoIds().isEmpty());
    }

    @Test
    void shouldHandleHeader() {
        BookHeader header = new BookHeader();
        header.setTitle("Test Book");
        ImportItem item = new ImportItem(header);

        ValidationProcessor.ValidationData data = processor.process(item);

        assertNotNull(data);
        assertEquals("Test Book", data.getBookTitle());
    }

    @Test
    void shouldThrowExceptionForNullSectionId() {
        SectionJson section = new SectionJson();
        section.setId(null);
        ImportItem item = new ImportItem(section);

        assertThrows(IllegalArgumentException.class, () -> processor.process(item));
    }
}
