package com.pictet.adventurebook.game.svc.features.chooseoption;

import com.pictet.adventurebook.common.result.Result;
import com.pictet.adventurebook.game.domain.SessionState;
import com.pictet.adventurebook.game.domain.Session;
import com.pictet.adventurebook.game.domain.type.SessionId;
import com.pictet.adventurebook.game.persistence.api.SavedGameRepository;
import com.pictet.adventurebook.game.persistence.api.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionRetrieverTest {

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SavedGameRepository savedGameRepository;

    @InjectMocks
    private SessionRetriever sessionRetriever;

    @Test
    void shouldGetSessionFromRedis() {
        // Given
        Long sessionId = 1L;
        Session session = Session.builder()
                .id(SessionId.valueOf(sessionId))
                .state(SessionState.builder().health(100.0).build())
                .build();
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // When
        Result<Session> result = sessionRetriever.getSession(sessionId);

        // Then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEqualTo(session);
        verifyNoInteractions(savedGameRepository);
    }

    @Test
    void shouldRecoverSessionFromDbIfMissingInRedis() {
        // Given
        Long sessionId = 1L;
        Session session = Session.builder()
                .id(SessionId.valueOf(sessionId))
                .state(SessionState.builder().health(100.0).build())
                .build();
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(savedGameRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // When
        Result<Session> result = sessionRetriever.getSession(sessionId);

        // Then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEqualTo(session);
        verify(sessionRepository).save(session);
    }

    @Test
    void shouldReturnFailureIfSessionNotFoundInEither() {
        // Given
        Long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(savedGameRepository.findById(sessionId)).thenReturn(Optional.empty());

        // When
        Result<Session> result = sessionRetriever.getSession(sessionId);

        // Then
        assertThat(result.isFailure()).isTrue();
    }
}
