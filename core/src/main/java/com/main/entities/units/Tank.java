package com.main.entities.units;

import java.util.ArrayList;
import java.util.List;

public class Tank extends Soldier {
    // private Animation<TextureRegion> walkLeft;
    // private float stateTime = 0f;
    // private boolean moving = false;
    // private List<Texture> loadedTextures = new ArrayList<>();
    // private final float FRAME_DURATION = 0.2f;

    public Tank(float posX, float posY) {
        super("Tank/tank.png", posX, posY);
        this.health = 500;
        this.attackDamage = 30;
        this.speed = 15;
        this.attackSpeed = 3;
        this.range = 100;
    }

}