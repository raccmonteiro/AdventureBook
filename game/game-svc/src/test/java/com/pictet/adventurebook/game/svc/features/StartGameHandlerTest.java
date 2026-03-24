package com.pictet.adventurebook.game.svc.features;

import com.pictet.adventurebook.common.result.Result;
import com.pictet.adventurebook.game.domain.SessionState;
import com.pictet.adventurebook.game.domain.SectionWithOptions;
import com.pictet.adventurebook.game.domain.Session;
import com.pictet.adventurebook.game.domain.SessionMaterialized;
import com.pictet.adventurebook.game.domain.type.SectionId;
import com.pictet.adventurebook.game.domain.type.SectionText;
import com.pictet.adventurebook.game.domain.type.SessionId;
import com.pictet.adventurebook.game.persistence.api.SavedGameRepository;
import com.pictet.adventurebook.game.persistence.api.SectionRepository;
import com.pictet.adventurebook.game.persistence.api.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartGameHandlerTest {

    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SavedGameRepository savedGameRepository;

    @InjectMocks
    private StartGameHandler startGameHandler;

    @Test
    void shouldStartGameBySavingToDbThenRedis() {
        // Given
        Long bookId = 1L;
        SectionWithOptions section = SectionWithOptions.builder()
                .id(SectionId.valueOf(10L))
                .text(SectionText.valueOf("Welcome"))
                .build();
        
        Session dbSession = Session.builder()
                .id(SessionId.valueOf(999L))
                .sectionId(section.getId())
                .state(SessionState.builder().health(100.0).build())
                .build();

        when(sectionRepository.findFirstSectionByBookIdWithOptions(bookId)).thenReturn(Optional.of(section));
        when(savedGameRepository.save(any(Session.class))).thenReturn(dbSession);

        // When
        Result<SessionMaterialized> result = startGameHandler.handle(bookId);

        // Then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().getId().getValue()).isEqualTo(999L);
        
        verify(savedGameRepository).save(any(Session.class));
        verify(sessionRepository).save(dbSession);
    }
}
