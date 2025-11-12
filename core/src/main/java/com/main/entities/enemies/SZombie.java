package com.main.entities.enemies;

public class SZombie extends Zombie {

    public SZombie(String filePath, int posX, int posY) {
        super(filePath, posX, posY);
        this.health = 350; // Augmenté pour être plus résistant
        this.speed = 2;
        this.attackDamage = 20; 
        this.attackSpeed = 1.2f; // 1.2 seconds between attacks (slower zombie)
        this.range = 50; // Portée égale au Melee
    }
}
