package com.main.entities.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Melee extends Soldier {

    public Melee(float posX, float posY) {
        super("Melee/Walk1.png", posX, posY);
        this.health = 200;
        this.attackDamage = 20;
        this.speed = 100;
        this.attackSpeed = 1.0f; // 1 second between attacks
        this.range = 50; // Portée courte pour mêlée

        // Load walk animation
        TextureRegion[] walkFrames = loadFrames("Melee/Walk%d.png", 8);
        this.walkAnimation = new Animation<>(FRAME_DURATION, walkFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // Load attack animation
        TextureRegion[] attackFrames = loadFrames("Melee/Attack_%d.png", 10);
        this.attackAnimation = new Animation<>(0.15f, attackFrames);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // Load idle frame
        Texture idleTex = new Texture(Gdx.files.internal("Melee/Walk1.png"));
        loadedTextures.add(idleTex);
        this.idleFrame = new TextureRegion(idleTex);
    }

    @Override
    protected float getAttackAnimationDuration() {
        if (this.attackAnimation != null) {
            return this.attackAnimation.getAnimationDuration();
        }
        return super.getAttackAnimationDuration();
    }
}
