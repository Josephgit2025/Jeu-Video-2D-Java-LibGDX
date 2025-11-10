package com.main.entities.enemies;

public class SZombie extends Zombie {

    public SZombie(String filePath, int posX, int posY) {
        super(filePath, posX, posY);
        this.health = 200;
        this.speed = 2;
        this.attackDamage = 10; 
        this.attackSpeed = 2;
        this.range = 3;
    }
}
