package com.main.entities.units;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.entities.Unit;

public class Soldier extends Unit {
    // Animation fields - shared by all soldier types
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected TextureRegion idleFrame;
    protected float stateTime = 0f;
    protected List<Texture> loadedTextures = new ArrayList<>();
    protected final float FRAME_DURATION = 0.2f;
 
    public Soldier (String filePath, float posX, float posY) {
        super(filePath, posX, posY);
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

        // Check if should stop (using parent logic)
        if (shouldStopMoving()) {
            currentState = UnitState.IDLE;
            this.stateTime += delta;
            return;
        }

        // Move right (soldiers direction) with collision check
        currentState = UnitState.WALKING;
        float newX = calculateNewPositionX(delta, 1); // 1 for right movement
        this.setSpritePosX(newX);
        this.stateTime += delta;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = getCurrentFrame();
        if (currentFrame != null) {
            batch.draw(currentFrame, this.posX, this.posY);
        }
    }

    /**
     * Get the current frame based on state - can be overridden by subclasses
     * for specific rendering behavior (e.g., Tank with custom size)
     */
    protected TextureRegion getCurrentFrame() {
        switch (getCurrentState()) {
            case ATTACKING:
                if (attackAnimation != null) {
                    return attackAnimation.getKeyFrame(stateTime, false);
                }
                break;
            case IDLE:
                if (idleFrame != null) {
                    return idleFrame;
                }
                break;
            case WALKING:
            default:
                if (walkAnimation != null) {
                    return walkAnimation.getKeyFrame(stateTime, true);
                }
                break;
        }
        // Fallback to first walk frame
        return walkAnimation != null ? walkAnimation.getKeyFrame(0, false) : null;
    }

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
