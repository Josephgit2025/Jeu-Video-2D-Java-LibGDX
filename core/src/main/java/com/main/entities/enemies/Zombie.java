package com.main.entities.enemies;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.entities.Unit;

public class Zombie extends Unit {
    // Animation fields - shared by all zombie types
    protected Animation<TextureRegion> walkLeft;
    protected Animation<TextureRegion> attackAnimation;
    protected TextureRegion attackFrame;
    protected TextureRegion idleFrame;
    protected float stateTime = 0f;
    protected boolean moving = false;
    protected List<Texture> loadedTextures = new ArrayList<>();
    protected final float FRAME_DURATION = 0.2f;

    public Zombie (String filePath, float posX, float posY) {
        super(filePath, posX, posY);
    }

    @Override
    public void move(float delta) {
        this.moving = false; // Reset moving state
        
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
        
        // Move left (zombies direction) with collision check
        currentState = UnitState.WALKING;
        float newX = calculateNewPositionX(delta, -1); // -1 for left movement
        this.setSpritePosX(newX);
        this.moving = true;
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
     * for specific animation behavior
     */
    protected TextureRegion getCurrentFrame() {
        switch (getCurrentState()) {
            case ATTACKING:
                // If attackAnimation exists, use it; otherwise use attackFrame
                if (attackAnimation != null) {
                    return attackAnimation.getKeyFrame(stateTime, false);
                } else if (attackFrame != null) {
                    return attackFrame;
                }
                break;
            case IDLE:
                // Use idleFrame if it exists, otherwise attackFrame
                if (idleFrame != null) {
                    return idleFrame;
                } else if (attackFrame != null) {
                    return attackFrame;
                }
                break;
            case WALKING:
            default:
                if (walkLeft != null) {
                    return walkLeft.getKeyFrame(stateTime, true);
                }
                break;
        }
        // Fallback to first walk frame
        return walkLeft != null ? walkLeft.getKeyFrame(0, false) : null;
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
