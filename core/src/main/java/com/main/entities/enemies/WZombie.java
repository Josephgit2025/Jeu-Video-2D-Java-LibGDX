package com.main.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

        TextureRegion[] attackTex = loadFrames("zombie/women/Attack%d.png", 20);
        attackAnimation = new Animation<>(0.1f, attackTex);
        // Attack should play once when triggered
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        Texture idleTex = new Texture(Gdx.files.internal("zombie/normal/Idle.png"));
        this.idleFrame = new TextureRegion(idleTex);
    }
}
