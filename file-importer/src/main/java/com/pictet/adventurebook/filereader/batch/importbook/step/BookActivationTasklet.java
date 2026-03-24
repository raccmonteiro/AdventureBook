package com.pictet.adventurebook.filereader.batch.importbook.step;

import com.pictet.adventurebook.book.domain.type.BookStatus;
import com.pictet.adventurebook.book.persistence.repository.spring.BookSpringRepository;
import com.pictet.adventurebook.filereader.batch.BookImportProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookActivationTasklet implements Tasklet {

    private final BookSpringRepository bookSpringRepository;
    private final BookImportProcessor processor;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Long bookId = processor.getCurrentBookId();
        if (bookId == null) {
            log.error("Cannot activate book: currentBookId is null in BookImportProcessor");
            throw new IllegalStateException("BookId is not set. Book insertion step must run before activation.");
        }

        log.info("Activating book with ID: {}", bookId);
        bookSpringRepository.updateBookStatus(bookId, BookStatus.ACTIVE.getId());
        log.info("Book with ID {} activated successfully", bookId);

        return RepeatStatus.FINISHED;
    }
}
