package com.main.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FZombie extends Zombie {

    public FZombie(int posX, int posY) {
        super("zombie/normal/Walk1.png", posX, posY);
        this.health = 450;
        this.speed = 80;
        this.attackDamage = 25;
        this.attackSpeed = 0.8f; // 0.8 seconds between attacks (fast zombie)
        this.range = 50;
        
        // Load walk animation
        TextureRegion[] leftFrames = loadFrames("zombie/normal/Walk%d.png", 10);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);

        // Load attack animation
        Texture attackTex = new Texture(Gdx.files.internal("zombie/normal/Attack.png"));
        loadedTextures.add(attackTex);
        this.attackAnimation = new Animation<>(0.2f, new TextureRegion(attackTex));
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // Load idle frame
        Texture idleTex = new Texture(Gdx.files.internal("zombie/normal/Idle.png"));
        loadedTextures.add(idleTex);
        this.idleFrame = new TextureRegion(idleTex);
    }
}
