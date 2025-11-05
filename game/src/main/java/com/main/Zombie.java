package com.main;

public class Zombie extends Unit {

    public Zombie (int posX, int posY) {
        super("/com/main/assets/Zombie.png", posX, posY);
        this.health = 100;
        this.attackDamage = 15;
        this.speed = 2;
        this.attackSpeed = 1;
    }

}
