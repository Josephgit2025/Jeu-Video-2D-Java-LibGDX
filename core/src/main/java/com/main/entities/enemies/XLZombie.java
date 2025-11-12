package com.main.entities.enemies;

public class XLZombie extends Zombie {

    public XLZombie(String filePath, int posX, int posY) {
        super(filePath, posX, posY);
        this.health = 500;
        this.speed = 1;
        this.attackDamage = 20; 
        this.attackSpeed = 3;
        this.range = 150; // Portée courte pour zombie
    }

}
