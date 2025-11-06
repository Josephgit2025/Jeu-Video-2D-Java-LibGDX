package com.main.entities.units;

import java.util.ArrayList;

public class Melee extends Soldier {
    
    public Melee (int posX, int posY) {
        super("/com/main/assets/Melle.png", posX, posY);
        this.health = 200;
        this.attackDamage = 20;
        this.speed = 5;
        this.attackSpeed = 2;
        this.range = 80;
        // this.modifiers = new ArrayList<>();
    }

}