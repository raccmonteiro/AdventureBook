package com.pictet.adventurebook.game.svc.mechanics;

import com.pictet.adventurebook.game.domain.Consequence;
import com.pictet.adventurebook.game.domain.SessionState;

public abstract class Mechanic {

    abstract String getName();

    abstract void apply(SessionState playerStats, Consequence consequence);


}
