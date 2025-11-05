package com.main;

import java.util.ArrayList;
import java.util.List;

public class Melee extends Soldier {
    
    public Melee (int posX, int posY) {
        super("/com/main/assets/Melle.png", posX, posY);
        this.health = 200;
        this.attackDamage = 20;
        this.speed = 5;
        this.attackSpeed = 2;
        this.modifiers = new ArrayList<>();
    }

}
