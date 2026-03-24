package com.pictet.adventurebook.book.svc.features.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.pictet.adventurebook.book.persistence.api.BookFileContentRepository;
import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.TeeInputStream;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadBookHandler {

    private final Executor virtualThreadsExecutor;
    private final BookFileContentRepository bookFileContentRepository;
    private final JsonValidator jsonValidator;
    private final TransactionTemplate transactionTemplate;
    private final BookImporterJobLauncher bookImporterJobLauncher;

    public Result<Void> handle(InputStream inputStream) {
        final Result<Long> transactionResult = transactionTemplate.execute(status -> {
            try (PipedOutputStream copyOut = new PipedOutputStream();
                 PipedInputStream secondIn = new PipedInputStream(copyOut);
                 TeeInputStream teeInputStream = new TeeInputStream(inputStream, copyOut)
            ) {

                CompletableFuture<Long> writeIntoDbFuture = CompletableFuture.supplyAsync(
                        () -> bookFileContentRepository.save(secondIn), virtualThreadsExecutor);

                boolean isValid;
                try {
                    isValid = jsonValidator.isValidJson(teeInputStream);
                } finally {
                    // close the output stream to release resources, otherwise the input stream will block
                    copyOut.close();
                }

                if (!isValid) {
                    log.warn("Uploaded book file content is not valid JSON");
                    writeIntoDbFuture.cancel(true);
                    status.setRollbackOnly();
                    return Result.failure(ErrorCode.INVALID_INPUT, "Invalid JSON format");
                }

                final Long fileId = writeIntoDbFuture.join();

                if (fileId == null) {
                    log.error("Failed to save book content");
                    status.setRollbackOnly();
                    return Result.failure(ErrorCode.INTERNAL_ERROR, "Failed to save book content");
                }

                return Result.success(fileId);

            } catch (IOException e) {
                log.error("Error during parallel upload processing", e);
                status.setRollbackOnly();
                return Result.failure(ErrorCode.INTERNAL_ERROR, "Stream processing error");
            } catch (Exception e) {
                log.error("Unexpected error during parallel upload processing", e);
                status.setRollbackOnly();
                return Result.failure(ErrorCode.INTERNAL_ERROR, "Unexpected error");
            }
        });

        return transactionResult.fold(
            bookFileId -> {
                bookImporterJobLauncher.launch(bookFileId);
                return Result.success(null);
            },
            error -> {
                log.error("Transaction failed: {}", error.getMessage());
                return Result.failure(error);
            }
        );

    }

}
