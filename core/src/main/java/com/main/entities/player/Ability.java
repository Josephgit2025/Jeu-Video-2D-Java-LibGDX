package com.main.entities.player;

/**
 * Représente une capacité spéciale du héros
 */
public class Ability {
    protected String name;
    protected String description;
    protected int cooldown;
    protected int damage;
    protected int currentCooldown;

    public Ability(String name, String description, int cooldown, int damage) {
        this.name = name;
        this.description = description;
        this.cooldown = cooldown;
        this.damage = damage;
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

    public int getDamage() {
        return damage;
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
