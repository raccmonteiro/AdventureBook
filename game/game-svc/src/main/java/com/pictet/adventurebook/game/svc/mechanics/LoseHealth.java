package com.pictet.adventurebook.game.svc.mechanics;

import com.pictet.adventurebook.game.domain.Consequence;
import com.pictet.adventurebook.game.domain.SessionState;
import org.springframework.stereotype.Component;

@Component
class LoseHealth extends Mechanic {

    final String getName() {
        return "LOSE_HEALTH";
    }


    @Override
    void apply(SessionState playerStats, Consequence consequence) {
        double health = playerStats.getHealth();
        double damage = consequence.getValue().getValue();

        double newHealth = Math.max(0, health - damage);
        playerStats.setHealth(newHealth);
    }
}
