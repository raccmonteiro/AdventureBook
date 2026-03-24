package com.pictet.adventurebook.book.persistence.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookFileContentRepositoryImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private BookFileContentRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new BookFileContentRepositoryImpl(jdbcTemplate);
    }

    @Test
    void shouldHandleMultipleReturnedKeys() {
        InputStream inputStream = new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8));
        
        doAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            Map<String, Object> keys = new HashMap<>();
            keys.put("id", 5L);
            keys.put("created_at", "2026-03-22 09:48:05.370098");
            keys.put("content", "{}");
            // Mimic multiple keys returned
            keyHolder.getKeyList().add(keys);
            return 1;
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        Long id = repository.save(inputStream);

        assertEquals(5L, id);
    }

    @Test
    void shouldUpdateBookId() {
        Long fileId = 1L;
        Long bookId = 2L;

        repository.updateBookId(fileId, bookId);

        verify(jdbcTemplate).update(
                "UPDATE book_file_content SET book_id = ? WHERE id = ?",
                bookId, fileId
        );
    }
}
