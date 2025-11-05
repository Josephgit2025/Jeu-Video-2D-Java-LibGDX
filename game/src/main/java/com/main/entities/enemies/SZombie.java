package com.main.entities.enemies;

public class SZombie extends Zombie {

    public SZombie(int posX, int posY) {
        super("/com/main/assets/SZombie.png", posX, posY);
        this.health = 200;
        this.speed = 2;
        this.attackDamage = 10; 
        this.attackSpeed = 2;
        this.range = 3;
    }
}
