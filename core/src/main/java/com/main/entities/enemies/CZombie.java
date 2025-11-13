package com.main.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CZombie extends Zombie {

    public CZombie(int posX, int posY) {
        super("zombie/crawl/Walk1.png", posX, posY);
        this.health = 300;
        this.speed = 40;
        this.attackDamage = 15;
        this.attackSpeed = 1.2f; // 1.2 seconds between attacks (slower zombie)
        this.range = 50;

        // Load walk animation
        TextureRegion[] leftFrames = loadFrames("zombie/crawl/Walk%d.png", 10);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] attackTex = loadFrames("zombie/crawl/Attack%d.png", 12);
        attackAnimation = new Animation<>(FRAME_DURATION, attackTex);
        attackAnimation.setPlayMode(Animation.PlayMode.LOOP);

        Texture idleTex = new Texture(Gdx.files.internal("zombie/crawl/Walk1.png"));
        this.idleFrame = new TextureRegion(idleTex);
    }
}
