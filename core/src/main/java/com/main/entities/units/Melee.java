package com.main.entities.units;

public class Melee extends Soldier {
    
    public Melee (float posX, float posY) {
        super("zombie/women/Walk2.png", posX, posY);
        this.health = 200;
        this.attackDamage = 20;
        this.speed = 45;
        this.attackSpeed = 2;
        this.range = 80;
        // this.modifiers = new ArrayList<>(); // TODO: Créer la classe Effect si nécessaire
    }

}