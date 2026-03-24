package com.pictet.adventurebook.game.persistence.repository;

import com.pictet.adventurebook.game.domain.GameStatus;
import com.pictet.adventurebook.game.domain.Session;
import com.pictet.adventurebook.game.domain.SessionState;
import com.pictet.adventurebook.game.domain.type.SectionId;
import com.pictet.adventurebook.game.domain.type.SessionId;
import com.pictet.adventurebook.game.persistence.entity.SavedGameEntity;
import com.pictet.adventurebook.game.persistence.repository.spring.SavedGameSpringRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SavedGameRepositoryImplTest {

    @Mock
    private SavedGameSpringRepository springRepository;

    @InjectMocks
    private SavedGameRepositoryImpl savedGameRepository;

    @Test
    void shouldSaveSessionAndReturnDBId() {
        // Given
        SessionState stats = SessionState.builder().health(80.0).build();
        Session session = Session.builder()
                .sectionId(SectionId.valueOf(100L))
                .state(stats)
                .status(GameStatus.IN_PROGRESS)
                .build();

        SavedGameEntity savedEntity = SavedGameEntity.builder()
                .id(999L)
                .sectionId(100L)
                .state(stats)
                .status(GameStatus.IN_PROGRESS.getId())
                .build();

        when(springRepository.save(any(SavedGameEntity.class))).thenReturn(savedEntity);

        // When
        Session saved = savedGameRepository.save(session);

        // Then
        assertThat(saved.getId().getValue()).isEqualTo(999L);
        assertThat(saved.getState().getHealth()).isEqualTo(80.0);
        assertThat(saved.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
        
        ArgumentCaptor<SavedGameEntity> captor = ArgumentCaptor.forClass(SavedGameEntity.class);
        verify(springRepository).save(captor.capture());

        SavedGameEntity captured = captor.getValue();
        assertThat(captured.getId()).isNull();
        assertThat(captured.getSectionId()).isEqualTo(100L);
        assertThat(captured.getState()).isEqualTo(stats);
        assertThat(captured.getStatus()).isEqualTo('I');
    }
}
