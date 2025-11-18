package com.main.entities.units;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.entities.Unit;
import com.main.map.Base;

public class Soldier extends Unit {
    // Animation fields - shared by all soldier types
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected TextureRegion idleFrame;
    protected Animation<TextureRegion> idleFramer;
    protected List<Texture> loadedTextures = new ArrayList<>();
    protected final float FRAME_DURATION = 0.2f;

    public Soldier(String filePath, float posX, float posY, Base allyBase) {
        super(filePath, posX, posY);
        this.allyBase = allyBase;
    }

    @Override
    public void move(float delta) {
        // Update attack animation timer
        if (attackAnimationTimer > 0) {
            attackAnimationTimer -= delta;
            this.stateTime += delta;
            if (attackAnimationTimer <= 0) {
                currentState = UnitState.WALKING;
                this.stateTime = 0;
            }
            return;
        }
        // If there's a living unit target, handle engagement
        if (target != null && !target.isDead()) {
            double distance = Math
                    .sqrt(Math.pow(this.posX - target.getPosX(), 2) + Math.pow(this.posY - target.getPosY(), 2));
            if (distance <= this.range) {
                // In range: use shared attack logic so damage, cooldown and attack animation
                // timer are applied
                if (attackCooldown <= 0f) {
                    attack();
                    this.stateTime = 0f;
                } else {
                    currentState = UnitState.IDLE;
                    this.stateTime += delta;
                }
                return;
            }
        }

        // If attack animation or cooldown stopped us, check generic stop condition
        if (shouldStopMoving()) {
            currentState = UnitState.IDLE;
            this.stateTime += delta;
            return;
        }

        // Default movement: move right (soldier direction) with collision check
        currentState = UnitState.WALKING;
        float newX = calculateNewPositionX(delta, 1);
        this.setSpritePosX(newX);
        this.stateTime += delta;
    }

    @lombok.Generated
    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame;

        // Choose animation based on current state
        switch (getCurrentState()) {
            case ATTACKING:
                if (attackAnimation != null) {
                    currentFrame = attackAnimation.getKeyFrame(this.stateTime, false);
                } else if (idleFramer != null) {
                    currentFrame = idleFramer.getKeyFrame(this.stateTime, true);
                } else {
                    currentFrame = idleFrame;
                }
                break;
            case IDLE:
                if (idleFramer != null) {
                    currentFrame = idleFramer.getKeyFrame(this.stateTime, true);
                } else {
                    currentFrame = idleFrame;
                }
                break;
            case WALKING:
            default:
                if (walkAnimation != null) {
                    currentFrame = walkAnimation.getKeyFrame(this.stateTime, true);
                } else {
                    currentFrame = idleFrame;
                }
                break;
        }

        batch.draw(currentFrame, posX, posY);
    }

    /**
     * Get the current frame based on state - can be overridden by subclasses
     * for specific rendering behavior (e.g., Tank with custom size)
     */

    /**
     * Utility method to load animation frames from a pattern
     */
    protected TextureRegion[] loadFrames(String pattern, int count) {
        TextureRegion[] frames = new TextureRegion[count];
        for (int i = 0; i < count; i++) {
            Texture tex = new Texture(Gdx.files.internal(String.format(pattern, i + 1)));
            loadedTextures.add(tex);
            frames[i] = new TextureRegion(tex);
        }
        return frames;
    }

    @lombok.Generated
    @Override
    public void dispose() {
        super.dispose();
        // Dispose all loaded textures
        for (Texture tex : loadedTextures) {
            if (tex != null) {
                tex.dispose();
            }
        }
        loadedTextures.clear();
    }
}
