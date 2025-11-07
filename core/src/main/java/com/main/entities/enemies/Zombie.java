package com.main.entities.enemies;

import com.main.entities.Unit;

public class Zombie extends Unit {

    public Zombie (String filePath, float posX, float posY) {
        super(filePath, posX, posY);
    }

    @Override
    public void move(float delta){
        // zombies move leftwards by default
        this.setSpritePosX(this.posX - this.speed * delta);
    }
}
