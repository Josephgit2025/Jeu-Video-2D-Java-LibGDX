package com.main.entities.enemies;

public class FZombie extends Zombie {

    public FZombie(int posX, int posY) {
        super("zombie/women/Walk1.png", posX, posY);
        this.health = 500;
        this.speed = 200;
        this.attackDamage = 20; 
        this.attackSpeed = 3;
        this.range = 2;
    }

}
