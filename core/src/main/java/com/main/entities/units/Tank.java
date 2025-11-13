package com.main.entities.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tank extends Soldier {

    public Tank(float posX, float posY) {
        super("Tank/Ride1.png", posX, posY);
        this.health = 500;
        this.attackDamage = 30;
        this.speed = 15;
        this.attackSpeed = 3.0f; // 3 seconds between attacks (slow heavy weapon)
        this.range = 100; // Portée moyenne

        // Load walk animation
        TextureRegion[] walkFrames = loadFrames("Tank/Ride%d.png", 2);
        walkAnimation = new Animation<>(FRAME_DURATION, walkFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // Use spritesheet for attack animation (muzzle flash effect)
        TextureRegion[] attackFrames = loadFrames("Tank/Attack%d.png", 6);
        attackAnimation = new Animation<>(FRAME_DURATION, attackFrames);
        // Attack should play once per attack, not loop
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // Idle: reuse walk frames if no dedicated idle frames are provided
        if (walkFrames != null && walkFrames.length > 0) {
            this.idleFrame = walkFrames[0];
            this.idleFramer = new Animation<>(FRAME_DURATION, walkFrames);
            this.idleFramer.setPlayMode(Animation.PlayMode.LOOP);
        }
    }

    @Override
    public void render(SpriteBatch batch) {

        TextureRegion currentFrame;
        switch (getCurrentState()) {
            case ATTACKING:
                if (attackAnimation != null) {
                    currentFrame = attackAnimation.getKeyFrame(stateTime, false);
                } else if (idleFramer != null) {
                    currentFrame = idleFramer.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = idleFrame;
                }
                break;
            case IDLE:
                if (idleFramer != null) {
                    currentFrame = idleFramer.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = idleFrame;
                }
                break;
            case WALKING:
            default:
                if (walkAnimation != null) {
                    currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                } else if (idleFramer != null) {
                    currentFrame = idleFramer.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = idleFrame;
                }
                break;
        }

        float visualWidth = 85;
        float visualHeight = 50;

        float offsetX = (this.width - visualWidth) / 2;
        float offsetY = 0;

        batch.draw(currentFrame, this.posX + offsetX, this.posY + offsetY, visualWidth, visualHeight);
    }
}
