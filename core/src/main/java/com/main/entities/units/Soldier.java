package com.main.entities.units;

import com.main.entities.Unit;

public class Soldier extends Unit {
 
    public Soldier (float posX, float posY) {
        super(posX, posY);
    }

    public void move(){
        this.setSpritePosX(this.posX + this.speed);
    }
}