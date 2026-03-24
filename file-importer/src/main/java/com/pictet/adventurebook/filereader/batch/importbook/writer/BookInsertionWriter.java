package com.pictet.adventurebook.filereader.batch.importbook.writer;

import com.pictet.adventurebook.book.persistence.api.BookFileContentRepository;
import com.pictet.adventurebook.book.persistence.entity.BookEntity;
import com.pictet.adventurebook.book.persistence.repository.spring.BookSpringRepository;
import com.pictet.adventurebook.filereader.batch.BookImportProcessor;
import com.pictet.adventurebook.filereader.batch.importbook.dto.BookImportData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Step 2: Inserts book entities into database (one per JSON file)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BookInsertionWriter implements ItemWriter<BookImportData> {

    private final BookFileContentRepository bookFileContentRepository;
    private final BookSpringRepository bookSpringRepository;
    private final BookImportProcessor processor;

    @Override
    @Transactional
    public void write(Chunk<? extends BookImportData> chunk) {
        log.info("Step 2: Writing books for chunk of {} items to the database", chunk.size());
        try {
            for (BookImportData data : chunk) {
                if (data.getBook() != null) {
                    log.info("Saving book: '{}' by {}", data.getBook().getTitle(), data.getBook().getAuthor());

                    // Save Book
                    BookEntity savedBook = bookSpringRepository.save(data.getBook());
                    Long bookId = savedBook.getId();
                    log.info("Book '{}' saved with generated database ID: {}", data.getBook().getTitle(), bookId);

                    // Update book_file_content with bookId
                    Long fileId = processor.getCurrentFileId();
                    bookFileContentRepository.updateBookId(fileId, bookId);
                    log.info("Updated book_file_content with bookId {} for fileId {}", bookId, fileId);

                    // Store bookId for use in next steps
                    processor.setCurrentBookId(bookId);
                }
            }
        } catch (Exception e) {
            log.error("Error writing books to database", e);
            throw e;
        }
    }
}
