package com.pictet.adventurebook.game.svc.mechanics;

import com.pictet.adventurebook.game.domain.Consequence;
import com.pictet.adventurebook.game.domain.SessionState;
import com.pictet.adventurebook.game.domain.type.ConsequenceValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GainHealthTest {

    private final GainHealth mechanic = new GainHealth();

    @Test
    void shouldGainHealthButNotExceedMax() {
        SessionState stats = SessionState.builder().health(90).build();
        Consequence consequence = Consequence.builder()
                .value(ConsequenceValue.valueOf(20.0))
                .build();

        mechanic.apply(stats, consequence);

        // Wait, GainHealth implementation was: double newHealth = Math.max(MAX_HEALTH, health + gainedHealth);
        // That looks like a bug in GainHealth.java! It should be Math.min(MAX_HEALTH, health + gainedHealth);
        // If health=90, gained=20, health+gained=110. Math.max(100, 110) = 110.
        // If health=50, gained=20, health+gained=70. Math.max(100, 70) = 100.
        // Yes, GainHealth.java is definitely buggy.
        
        // I will write the test to expect what it *should* do, and then fix the code.
        assertEquals(100, stats.getHealth());
    }
}
