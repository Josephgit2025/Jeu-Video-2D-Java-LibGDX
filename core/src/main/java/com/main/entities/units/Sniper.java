package com.main.entities.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.entities.Unit;

public class Sniper extends Soldier {
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> attackAnimation;
    private TextureRegion idleFrame;
    private final float frameDuration = 0.2f;
    private float stateTime = 0f;

    public Sniper(int posX, int posY) {
        super("Sniper/Walk1.png", posX, posY);
        this.health = 150;
        this.attackDamage = 40;
        this.speed = 30;
        this.attackSpeed = 3;
        this.range = 500; // Portée longue pour sniper
        
        // Load animations
        TextureRegion[] walkFrames = loadFrames("Sniper/Walk%d.png", 8);
        this.walkAnimation = new Animation<>(frameDuration, walkFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        
        TextureRegion[] attackFrames = loadFrames("Sniper/Shot_%d.png", 2);
        this.attackAnimation = new Animation<>(0.1f, attackFrames);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        
        Texture idleTex = new Texture(Gdx.files.internal("Sniper/Idle.png"));
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
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }
        
        batch.draw(currentFrame, posX, posY);
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
        
        // Check if we need to stop for attack
        if (target != null && !target.isDead()) {
            double distance = Math.sqrt(Math.pow(this.posX - target.getPosX(), 2) + Math.pow(this.posY - target.getPosY(), 2));
            if (distance <= this.range) {
                currentState = UnitState.IDLE;
                this.stateTime += delta;
                return;
            }
        }
        
        // Only move and animate if not in combat
        currentState = UnitState.WALKING;
        this.setSpritePosX(this.posX + this.speed * delta);
        this.stateTime = this.stateTime + delta;
    }
}