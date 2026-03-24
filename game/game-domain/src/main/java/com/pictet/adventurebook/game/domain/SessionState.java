package com.pictet.adventurebook.game.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionState {

    private double health;


    @JsonIgnore
    public boolean isAlive() {
        return health > 0;
    }
}
