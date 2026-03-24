package com.pictet.adventurebook.book.svc.features.book;

import com.pictet.adventurebook.book.persistence.api.BookFileContentRepository;
import com.pictet.adventurebook.book.svc.features.upload.BookImporterJobLauncher;
import com.pictet.adventurebook.book.svc.features.upload.JsonValidator;
import com.pictet.adventurebook.book.svc.features.upload.UploadBookHandler;
import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadBookHandlerTest {

    @Mock
    private BookFileContentRepository repository;

    @Mock
    private TransactionTemplate transactionTemplate;

    @Mock
    private JsonValidator jsonValidator;

    @Mock
    private BookImporterJobLauncher bookImporterJobLauncher;

    private UploadBookHandler handler;

    private final Executor sameThreadExecutor = Runnable::run;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        // Mock transactionTemplate.execute to run the callback directly
        when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
            TransactionCallback<Result<Long>> callback = invocation.getArgument(0);
            return callback.doInTransaction(mock(TransactionStatus.class));
        });
        
        handler = new UploadBookHandler(sameThreadExecutor, repository, jsonValidator,
            transactionTemplate, bookImporterJobLauncher);
    }

    @Test
    void shouldUploadValidJson() {
        String json = "{\"title\": \"Test Book\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        when(jsonValidator.isValidJson(any(InputStream.class))).thenReturn(true);
        when(repository.save(any(InputStream.class))).thenReturn(1L);

        Result<Void> result = handler.handle(inputStream);

        assertTrue(result.isSuccess());
        verify(repository).save(any(InputStream.class));
        verify(jsonValidator).isValidJson(any(InputStream.class));
        verify(bookImporterJobLauncher).launch(1L);
    }

    @Test
    void shouldFailForInvalidJson() {
        String invalidJson = "{invalid}";
        InputStream inputStream = new ByteArrayInputStream(invalidJson.getBytes(StandardCharsets.UTF_8));

        when(jsonValidator.isValidJson(any(InputStream.class))).thenReturn(false);

        Result<Void> result = handler.handle(inputStream);

        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.INVALID_INPUT, result.getError().getCode());
        verify(bookImporterJobLauncher, never()).launch(anyLong());
    }

    @Test
    void shouldFailWhenRepositoryFailsToSave() {
        String json = "{\"title\": \"Test Book\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        when(jsonValidator.isValidJson(any(InputStream.class))).thenReturn(true);
        when(repository.save(any(InputStream.class))).thenReturn(null);

        Result<Void> result = handler.handle(inputStream);

        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.INTERNAL_ERROR, result.getError().getCode());
        verify(bookImporterJobLauncher, never()).launch(anyLong());
    }

    @Test
    void shouldFailWhenRepositoryThrowsException() {
        String json = "{\"title\": \"Test Book\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        when(jsonValidator.isValidJson(any(InputStream.class))).thenReturn(true);
        when(repository.save(any(InputStream.class))).thenThrow(new RuntimeException("DB error"));

        Result<Void> result = handler.handle(inputStream);

        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.INTERNAL_ERROR, result.getError().getCode());
        verify(bookImporterJobLauncher, never()).launch(anyLong());
    }
}
