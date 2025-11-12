package com.main.entities.enemies;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FZombie extends Zombie {
    private Animation<TextureRegion> walkLeft;
    private Animation<TextureRegion> attackAnimation;
    private TextureRegion idleFrame;
    private float stateTime = 0f;
    private boolean moving = false;
    private List<Texture> loadedTextures = new ArrayList<>();
    private final float FRAME_DURATION = 0.2f;

    public FZombie(int posX, int posY) {
        super("zombie/normal/Walk1.png", posX, posY);
        this.health = 450; // Augmenté pour être plus résistant
        this.speed = 80;
        this.attackDamage = 25;
        this.attackSpeed = 0.8f; // 0.8 seconds between attacks (fast zombie)
        this.range = 50; // Portée égale au Melee
        
        // Load walk animation
        TextureRegion[] leftFrames = loadFrames("zombie/normal/Walk%d.png", 10);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);
        
        // Load attack animation
        Texture attackTex = new Texture(Gdx.files.internal("zombie/normal/Attack.png"));
        this.attackAnimation = new Animation<>(0.2f, new TextureRegion(attackTex));
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        
        // Load idle frame
        Texture idleTex = new Texture(Gdx.files.internal("zombie/normal/Idle.png"));
        this.idleFrame = new TextureRegion(idleTex);
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
        TextureRegion currentFrame;
        
        // Choose animation based on current state
        switch (getCurrentState()) {
            case ATTACKING:
                currentFrame = attackAnimation.getKeyFrame(stateTime, false);
                break;
            case IDLE:
                currentFrame = idleFrame;
                break;
            case WALKING:
            default:
                currentFrame = walkLeft.getKeyFrame(stateTime, true);
                break;
        }
        
        batch.draw(currentFrame, this.posX, this.posY);
    }

    private TextureRegion[] loadFrames(String pattern, int count) {
        TextureRegion[] frames = new TextureRegion[count];
        for (int i = 0; i < count; i++) {
            Texture tex = new Texture(Gdx.files.internal(String.format(pattern, i + 1)));
            loadedTextures.add(tex);
            frames[i] = new TextureRegion(tex);
        }
        return frames;
    }

}
