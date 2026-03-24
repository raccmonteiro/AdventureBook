package com.pictet.adventurebook.game.svc.mechanics;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.pictet.adventurebook.game.domain.Consequence;
import com.pictet.adventurebook.game.domain.SessionState;
import com.pictet.adventurebook.game.domain.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameMechanicsService {

    private final Map<String, Mechanic> mechanics;

    public GameMechanicsService(List<Mechanic> mechanics) {
        this.mechanics = mechanics.stream()
                .collect(Collectors.toMap(Mechanic::getName, mechanic -> mechanic));
    }

    public void applyConsequences(final Session session, final List<Consequence> consequences) {
        final SessionState playerStats = session.getState();

        if (consequences == null || consequences.isEmpty()) {
            return;
        }

        consequences.forEach(consequence ->
            Optional.ofNullable(mechanics.get(consequence.getType().getValue().toUpperCase()))
                    .ifPresentOrElse(
                        mechanic -> mechanic.apply(playerStats, consequence),
                        () -> log.warn("Unknown consequence type: {}", consequence.getType())
                    )
        );

        session.setState(playerStats);
    }
}
