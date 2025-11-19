package com.main.weapons;

/**
 * Represents an assault rifle weapon in the game.
 * <p>
 * Provides moderate damage, medium range, fast attack speed, and high ammunition capacity. Used by units for rapid fire.
 */
public class AssaultRifle extends Weapon {

    /**
     * Constructs an AssaultRifle with predefined stats (damage, range, attack speed, ammunition).
     * Calls the superclass constructor with assault rifle parameters.
     */
    public AssaultRifle() {
        super(30, 200, 1f, 30);
    }
}