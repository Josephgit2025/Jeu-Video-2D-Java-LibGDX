package com.main;

public class Sniper extends Soldier{

    public Sniper (int posX, int posY) {
        super("/com/main/assets/Sniper.png", posX, posY);
        this.health = 150;
        this.attackDamage = 40;
        this.speed = 4;
        this.attackSpeed = 3;
    }
}
