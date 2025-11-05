package com.main;

public class Tank extends Soldier {
    
    public Tank (String filePath, int posX, int posY) {
        super(filePath, posX, posY);
        this.health = 500;
        this.attackDamage = 10;
        this.speed = 3;
        this.attackSpeed = 1;
    }

}
