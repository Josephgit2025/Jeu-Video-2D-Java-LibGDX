package com.main.entities.enemies;

import com.main.entities.Unit;

public class Zombie extends Unit {

    public Zombie (String filePath, int posX, int posY) {
        super(filePath, posX, posY);
        this.health = 100;
        this.attackDamage = 15;
        this.speed = 2;
        this.attackSpeed = 1; 
    }

}