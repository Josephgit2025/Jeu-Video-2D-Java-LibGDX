package com.main.entities.units;

public class Sniper extends Soldier{

    public Sniper (int posX, int posY) {
        super("zombie/women/Walk3.png", posX, posY);
        this.health = 150;
        this.attackDamage = 40;
        this.speed = 30;
        this.attackSpeed = 3;
        this.range = 400;
    }
}