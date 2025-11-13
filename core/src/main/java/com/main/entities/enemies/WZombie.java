package com.main.entities.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WZombie extends Zombie {

    public WZombie(int posX, int posY) {
        super("zombie/women/Walk1.png", posX, posY);
        this.health = 350;
        this.speed = 130;
        this.attackDamage = 20;
        this.attackSpeed = 0.8f; // 0.8 seconds between attacks (fast zombie)
        this.range = 50;
        
        // Load walk animation
        TextureRegion[] leftFrames = loadFrames("zombie/women/Walk%d.png", 7);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);
        
        // Use first frame as attack pose
        this.attackFrame = leftFrames[0];
    }
}
