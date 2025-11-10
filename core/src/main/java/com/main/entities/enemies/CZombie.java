package com.main.entities.enemies;

public class CZombie extends Zombie {

    public CZombie(int posX, int posY) {
        super("zombie/women/Walk1.png", posX, posY);
        this.health = 200;
        this.speed = 40;
        this.attackDamage = 10; 
        this.attackSpeed = 2;
        this.range = 3;
    }
}
