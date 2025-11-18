package com.main.entities.enemies;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.entities.Unit;
import com.main.map.Base;

public class Zombie extends Unit {
        // Valeurs de base pour l’équilibrage
        public static final int HP_BASE = 200;
        public static final float DAMAGE_BASE = 30f;
        public static final float ATTACK_SPEED_BASE = 1.5f;
    // Animation fields - shared by all zombie types
    protected Animation<TextureRegion> walkLeft;
    protected Animation<TextureRegion> attackAnimation;
    protected TextureRegion attackFrame;
    protected TextureRegion idleFrame;
    protected boolean moving = false;
    protected List<Texture> loadedTextures = new ArrayList<>();
    protected final float FRAME_DURATION = 0.2f;

    public Zombie(String filePath, float posX, float posY, Base allyBase) {
        super(filePath, posX, posY);
        this.allyBase = allyBase;
    }

    @Override
    public void move(float delta) {
        this.moving = false; // Reset moving state

        // Update attack animation timer
        if (attackAnimationTimer > 0) {
            attackAnimationTimer -= delta;
            // advance shared animation time so attack animations progress
            this.stateTime += delta;
            if (attackAnimationTimer > 0) {
                return;
            }
        }

        // Vérifie si une cible est à portée, si oui, attaque (utilise Unit.attack()
        // pour gérer cooldowns/timers)

        // If there's a unit target and it's in range, attack
        if (target != null && !target.isDead()) {
            double distance = Math
                    .sqrt(Math.pow(this.posX - target.getPosX(), 2) + Math.pow(this.posY - target.getPosY(), 2));
            if (distance <= this.range) {
                // Use the shared attack logic so damage, cooldown and attack animation timer
                // are applied
                attack();
                // stateTime reset is handled by Unit.attack(); ensure we advance during this frame
                this.stateTime += delta;
                return;
            }
        }

        // Only move and animate if not in combat

        // If should stop (eg base in range or attack animation), idle
        if (shouldStopMoving()) {
            currentState = UnitState.IDLE;
            this.stateTime += delta;
            // Attaque la cible si elle est à portée
            if (target != null && !target.isDead()) {
                attack();
            }
            return;
        }

        // Default: move left (zombies direction) with collision check
        currentState = UnitState.WALKING;
        float newX = calculateNewPositionX(delta, -1); // -1 for left movement
        this.setSpritePosX(newX);
        this.moving = true;
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
                    currentFrame = attackAnimation.getKeyFrame(stateTime, false);
                } else if (attackFrame != null) {
                    currentFrame = attackFrame;
                } else if (idleFrame != null) {
                    currentFrame = idleFrame;
                } else {
                    currentFrame = walkLeft != null ? walkLeft.getKeyFrame(stateTime, true) : null;
                }
                break;
            case IDLE:
                if (idleFrame != null) {
                    currentFrame = idleFrame;
                } else if (idleFrame == null && walkLeft != null) {
                    currentFrame = walkLeft.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = null;
                }
                break;
            case WALKING:
            default:
                if (walkLeft != null) {
                    currentFrame = walkLeft.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = idleFrame;
                }
                break;
        }

        // draw the selected frame if any
        if (currentFrame != null) {
            batch.draw(currentFrame, this.posX, this.posY);
        }
    }

    /**
     * Get the current frame based on state - can be overridden by subclasses
     * for specific animation behavior
     */
    protected float getAttackAnimationDuration() {
        if (attackAnimation != null) {
            return attackAnimation.getAnimationDuration();
        }
        return super.getAttackAnimationDuration();
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
