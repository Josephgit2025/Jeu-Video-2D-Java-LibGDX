package com.main.entities.enemies;

public class XLZombie extends Zombie {

    public XLZombie(String filePath, int posX, int posY) {
        super(filePath, posX, posY);
        this.health = 800; // Augmenté pour être très résistant (tank zombie)
        this.speed = 1;
        this.attackDamage = 35; 
        this.attackSpeed = 1.5f; // 1.5 seconds between attacks (tank zombie)
        this.range = 50; // Portée égale au Melee
    }

}
