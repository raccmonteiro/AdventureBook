package com.pictet.adventurebook.game.svc.mechanics;

import com.pictet.adventurebook.game.domain.Consequence;
import com.pictet.adventurebook.game.domain.SessionState;
import com.pictet.adventurebook.game.domain.type.ConsequenceValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoseHealthTest {

    private final LoseHealth mechanic = new LoseHealth();

    @Test
    void shouldLoseHealthButNotGoBelowZero() {
        SessionState stats = SessionState.builder().health(10).build();
        Consequence consequence = Consequence.builder()
                .value(ConsequenceValue.valueOf(20.0))
                .build();

        mechanic.apply(stats, consequence);

        assertEquals(0, stats.getHealth());
    }

    @Test
    void shouldLoseHealth() {
        SessionState stats = SessionState.builder().health(50).build();
        Consequence consequence = Consequence.builder()
                .value(ConsequenceValue.valueOf(10.0))
                .build();

        mechanic.apply(stats, consequence);

        assertEquals(40, stats.getHealth());
    }
}
