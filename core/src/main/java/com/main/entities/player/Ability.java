package com.main.entities.player;

import com.main.effects.Effect;

/**
 * Représente une capacité spéciale du héros
 */
public class Ability {
    protected String name;
    protected String description;
    protected int cooldown;
    protected Effect effect;
    protected int currentCooldown;

    public Ability(String name, String description, int cooldown, Effect effect) {
        this.name = name;
        this.description = description;
        this.cooldown = cooldown;
        this.effect = effect;
        this.currentCooldown = 0;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Effect getEffect() {
        return effect;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }

    public boolean isReady() {
        return currentCooldown <= 0;
    }

    public void use() {
        if (isReady()) {
            currentCooldown = cooldown;
            // Logique d'utilisation de la capacité
        }
    }

    public void updateCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }
}

