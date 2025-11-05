package com.main;

import java.util.ArrayList;
import java.util.List;

public class Melee extends Soldier {
    
    public Melee (String filePath, int posX, int posY) {
        super(filePath, posX, posY);
        this.health = 200;
        this.attackDamage = 20;
        this.speed = 5;
        this.attackSpeed = 2;
        this.modifiers = new ArrayList<>();
    }

}
