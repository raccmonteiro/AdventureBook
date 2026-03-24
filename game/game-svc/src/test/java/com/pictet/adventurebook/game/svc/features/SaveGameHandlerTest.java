package com.pictet.adventurebook.game.svc.features;

import com.pictet.adventurebook.common.result.Result;
import com.pictet.adventurebook.game.domain.GameStatus;
import com.pictet.adventurebook.game.domain.Session;
import com.pictet.adventurebook.game.domain.SessionState;
import com.pictet.adventurebook.game.domain.type.SectionId;
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
class SaveGameHandlerTest {

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SavedGameRepository savedGameRepository;

    @InjectMocks
    private SaveGameHandler saveGameHandler;

    @Test
    void shouldSaveSessionToDatabase() {
        // Given
        Long sessionId = 1L;
        Session session = Session.builder()
                .id(SessionId.valueOf(sessionId))
                .sectionId(SectionId.valueOf(10L))
                .state(SessionState.builder().health(100.0).build())
                .status(GameStatus.IN_PROGRESS)
                .build();
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // When
        Result<Boolean> result = saveGameHandler.handle(sessionId);

        // Then
        assertThat(result.isSuccess()).isTrue();
        verify(savedGameRepository).save(session);
    }
}
