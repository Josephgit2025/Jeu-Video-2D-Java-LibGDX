package com.main.entities.units;

import java.util.ArrayList;
import java.util.List;

import com.main.entities.player.Ability;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.entities.Unit;

public class Melee extends Soldier {
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> idleAnimation;
    private final float frameDuration = 0.2f;
    // uses inherited stateTime from Unit
    private TextureRegion[] walkFrames;
    private TextureRegion[] attackFrames;

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

        this.attackFrames = loadFrames("Melee/Attack_%d.png", 10);
        this.attackAnimation = new Animation<>(0.15f, attackFrames);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL); // Play once
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
        // stateTime is advanced in move(delta) to keep timing consistent with logic
        TextureRegion currentFrame;

        // Choose animation based on current state
        switch (getCurrentState()) {
            case ATTACKING:
                currentFrame = attackAnimation.getKeyFrame(stateTime, false);
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
        // If currently playing attack animation, advance timer and stateTime, don't move
        if (attackAnimationTimer > 0f) {
            attackAnimationTimer -= delta;
            this.stateTime += delta;
            if (attackAnimationTimer <= 0f) {
                // attack animation finished; if target still alive and in range, we can attempt another attack next frame
                if (target != null && !target.isDead()) {
                    double distance = Math.hypot(this.posX - target.getPosX(), this.posY - target.getPosY());
                    if (distance <= this.range && attackCooldown <= 0f) {
                        attack();
                        this.stateTime = 0f;
                    } else {
                        currentState = UnitState.IDLE;
                    }
                } else {
                    currentState = UnitState.WALKING;
                }
            }
            return; // do not move while attack animation plays
        }

        // If we have a target and it's alive, decide between attacking or moving towards it
        if (target != null && !target.isDead()) {
            double distance = Math.hypot(this.posX - target.getPosX(), this.posY - target.getPosY());
            if (distance <= this.range) {
                // In range: attack if ready, otherwise stay idle (and keep animation time)
                if (attackCooldown <= 0f) {
                    attack();
                    this.stateTime = 0f;
                    return;
                } else {
                    currentState = UnitState.IDLE;
                    this.stateTime += delta;
                    return;
                }
            } else {
                // Not in range: walk towards target
                currentState = UnitState.WALKING;
                float direction = (target.getPosX() > posX) ? 1f : -1f;
                this.setSpritePosX(this.posX + direction * this.speed * delta);
                this.stateTime += delta;
                return;
            }
        }

        // No target: walk forward (player soldiers move to the right by default)
        currentState = UnitState.WALKING;
        this.setSpritePosX(this.posX + this.speed * delta);
        this.stateTime += delta;
    }

    @Override
    public void attack() {
        // Call base attack to apply damage/cooldown and set ATTACKING state
        super.attack();
        // If base set ATTACKING, override animation duration to the real animation length
        if (currentState == UnitState.ATTACKING) {
            attackAnimationTimer = attackAnimation.getAnimationDuration();
            stateTime = 0f;
        }
    }
    @Override
    protected float getAttackAnimationDuration() {
        return attackAnimation.getAnimationDuration();
    }
}