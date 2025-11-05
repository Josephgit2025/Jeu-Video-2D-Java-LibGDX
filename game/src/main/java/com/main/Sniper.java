package com.main;

public class Sniper extends Soldier{

    public Sniper (String filePath, int posX, int posY) {
        super(filePath, posX, posY);
        this.health = 150;
        this.attackDamage = 40;
        this.speed = 4;
        this.attackSpeed = 3;
    }
}
