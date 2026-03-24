package com.pictet.adventurebook.game.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStatsTest {

    @Test
    void shouldBeAliveWhenHealthIsPositive() {
        SessionState stats = SessionState.builder().health(10).build();
        assertTrue(stats.isAlive());
    }

    @Test
    void shouldNotBeAliveWhenHealthIsZero() {
        SessionState stats = SessionState.builder().health(0).build();
        assertFalse(stats.isAlive());
    }

    @Test
    void shouldNotBeAliveWhenHealthIsNegative() {
        SessionState stats = SessionState.builder().health(-5).build();
        assertFalse(stats.isAlive());
    }

    @Test
    void shouldSetHealth() {
        SessionState stats = SessionState.builder().health(100).build();
        stats.setHealth(50);
        assertEquals(50, stats.getHealth());
    }
}
