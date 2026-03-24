package com.pictet.adventurebook.game.persistence.redis.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventurebook.game.domain.GameStatus;
import com.pictet.adventurebook.game.domain.Session;
import com.pictet.adventurebook.game.domain.SessionState;
import com.pictet.adventurebook.game.domain.type.SectionId;
import com.pictet.adventurebook.game.domain.type.SessionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionRepositoryImplTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    private SessionRepositoryImpl sessionRepository;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        sessionRepository = new SessionRepositoryImpl(redisTemplate, objectMapper);
    }

    @Test
    void shouldSaveAndRetrieveSessionWithStatus() throws JsonProcessingException {
        // Given
        Long sessionId = 123L;
        Session session = Session.builder()
                .id(SessionId.valueOf(sessionId))
                .sectionId(SectionId.valueOf(10L))
                .state(SessionState.builder().health(85.0).build())
                .status(GameStatus.LOST)
                .build();

        // When
        sessionRepository.save(session);

        // Then
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(valueOperations).set(eq("session:123"), jsonCaptor.capture(), anyLong(), eq(TimeUnit.HOURS));
        
        String savedJson = jsonCaptor.getValue();
        assertThat(savedJson).contains("\"status\":\"LOST\"");

        // Mock retrieval
        when(valueOperations.get("session:123")).thenReturn(savedJson);

        // When
        Optional<Session> retrieved = sessionRepository.findById(sessionId);

        // Then
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getStatus()).isEqualTo(GameStatus.LOST);
        assertThat(retrieved.get().getState().getHealth()).isEqualTo(85.0);
    }

    @Test
    void shouldFallbackToInProgressIfStatusIsMissingInJson() throws JsonProcessingException {
        // Given
        Long sessionId = 123L;
        // JSON without status field
        String jsonMissingStatus = "{\"id\":123,\"sectionId\":10,\"state\":{\"health\":100.0}}";
        when(valueOperations.get("session:123")).thenReturn(jsonMissingStatus);

        // When
        Optional<Session> retrieved = sessionRepository.findById(sessionId);

        // Then
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }
}
