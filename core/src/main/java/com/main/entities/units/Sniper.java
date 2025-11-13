package com.main.entities.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sniper extends Soldier {

    public Sniper(int posX, int posY) {
        super("Sniper/Walk1.png", posX, posY);
        this.health = 150;
        this.attackDamage = 40;
        this.speed = 30;
        this.attackSpeed = 2.5f; // 2.5 seconds between attacks (slower sniper fire)
        this.range = 250; // Portée longue pour sniper

        // Load walk animation
        TextureRegion[] walkFrames = loadFrames("Sniper/Walk%d.png", 8);
        this.walkAnimation = new Animation<>(FRAME_DURATION, walkFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // Load attack animation
        TextureRegion[] attackFrames = loadFrames("Sniper/Attack%d.png", 7);
        this.attackAnimation = new Animation<>(0.1f, attackFrames);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // Texture idleTex = new Texture(Gdx.files.internal("Sniper/Idle.png"));
        // this.idleFrame = new TextureRegion(idleTex);

        TextureRegion[] idleFrames = loadFrames("Sniper/Idle%d.png", 8);
        this.idleFramer = new Animation<>(FRAME_DURATION, idleFrames);
        idleFramer.setPlayMode(Animation.PlayMode.LOOP);
    }
}
