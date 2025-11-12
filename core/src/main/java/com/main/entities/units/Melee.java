package com.main.entities.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Melee extends Soldier {
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> idleAnimation;
    private final float frameDuration = 0.2f;
    private float stateTime = 0f;
    private TextureRegion[] walkFrames;
    private TextureRegion[] attackFrames;
    private TextureRegion idleFrame;

    public Melee(float posX, float posY) {
        super("Melee/Walk1.png", posX, posY);
        this.health = 200;
        this.attackDamage = 20;
        this.speed = 100;
        this.attackSpeed = 1.0f; // 1 second between attacks
        this.range = 50; // Portée courte pour mêlée
        
        // Load animations
        this.walkFrames = loadFrames("Melee/Walk%d.png", 8);
        this.walkAnimation = new Animation<>(frameDuration, walkFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        
        this.attackFrames = loadFrames("Melee/Attack_%d.png", 3);
        this.attackAnimation = new Animation<>(0.15f, attackFrames);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL); // Play once
        
        Texture idleTex = new Texture(Gdx.files.internal("Melee/Idle.png"));
        this.idleFrame = new TextureRegion(idleTex);
    }

    private TextureRegion[] loadFrames(String pattern, int count) {
        TextureRegion[] frames = new TextureRegion[count];
        for (int i = 0; i < count; i++) {
            Texture tex = new Texture(Gdx.files.internal(String.format(pattern, i + 1)));
            frames[i] = new TextureRegion(tex);
        }
        return frames;
    }

    @Override
    public void render(SpriteBatch batch){
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
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }
        
        batch.draw(currentFrame, posX, posY);
    }

    @Override
    public void move(float delta){
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
        float newX = calculateNewPositionX(delta, 1);
        this.setSpritePosX(newX);
        this.stateTime += delta;
    }

}