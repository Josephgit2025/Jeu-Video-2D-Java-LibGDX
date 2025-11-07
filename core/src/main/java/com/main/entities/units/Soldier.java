package com.main.entities.units;

import com.main.entities.Unit;

public class Soldier extends Unit {
 
    public Soldier (String filePath, float posX, float posY) {
        super(filePath, posX, posY);
    }

    @Override
    public void move(float delta){
        // move to the right by default
        this.setSpritePosX(this.posX + this.speed * delta);
    }
}