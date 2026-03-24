package com.pictet.adventurebook.game.svc.mechanics;

import com.pictet.adventurebook.game.domain.Consequence;
import com.pictet.adventurebook.game.domain.SessionState;
import org.springframework.stereotype.Component;

@Component
class GainHealth extends Mechanic {
    
    final String getName() {
        return "GAIN_HEALTH";
    }

    private static final int MAX_HEALTH = 100;

    @Override
    void apply(SessionState playerStats, Consequence consequence) {
        double health = playerStats.getHealth();
        double gainedHealth = consequence.getValue().getValue();
        double newHealth = Math.min(MAX_HEALTH, health + gainedHealth);
        playerStats.setHealth(newHealth);
    }
}
