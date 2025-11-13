package com.main.entities.enemies;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WZombie extends Zombie {
    private Animation<TextureRegion> walkLeft;
    private Animation<TextureRegion> attackAnimation;
    private TextureRegion idleFrame;
    // uses inherited stateTime from Unit
    private List<Texture> loadedTextures = new ArrayList<>();
    private final float FRAME_DURATION = 0.2f;
    private TextureRegion[] walkFrames;
    private TextureRegion[] attackFrames;

    public WZombie(int posX, int posY) {
        super("zombie/women/Walk1.png", posX, posY);
        this.health = 350; // Augmenté pour être plus résistant
        this.speed = 130;
        this.attackDamage = 20;
        this.attackSpeed = 0.8f; // 0.8 seconds between attacks (fast zombie)
        this.range = 50; // Portée égale au Melee

        TextureRegion[] leftFrames = loadFrames("zombie/women/Walk%d.png", 7);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] attackTex = loadFrames("zombie/women/Attack%d.png", 20);
        attackAnimation = new Animation<>(0.1f, attackTex);
        // Attack should play once when triggered
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        Texture idleTex = new Texture(Gdx.files.internal("zombie/normal/Idle.png"));
        this.idleFrame = new TextureRegion(idleTex);
    }

    @Override
    public void move(float delta) {
        // If currently playing attack animation, advance timer and stateTime, don't
        // move
        if (attackAnimationTimer > 0f) {
            attackAnimationTimer -= delta;
            this.stateTime += delta;
            if (attackAnimationTimer <= 0f) {
                // attack animation finished; if target still alive and in range, we can attempt
                // another attack next frame
                if (target != null && !target.isDead()) {
                    double distance = Math.hypot(target.getPosX() - this.posX, target.getPosY() - this.posY);
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

        // If we have a target and it's alive, decide between attacking or moving
        // towards it
        if (target != null && !target.isDead()) {
            double distance = Math.hypot(target.getPosX() - this.posX, target.getPosY() - this.posY);
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

        // No target: walk towards the player base (zombies move left)
        currentState = UnitState.WALKING;
        this.setSpritePosX(this.posX - this.speed * delta);
        // Check if should stop (using parent logic)
        if (shouldStopMoving()) {
            currentState = UnitState.IDLE;
            this.stateTime += delta;
            return;
        }

        this.stateTime += delta;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame;

        // Choose frame based on current state
        switch (getCurrentState()) {
            case ATTACKING:
                currentFrame = attackAnimation.getKeyFrame(stateTime, false);
                break;
            case IDLE:
                currentFrame = idleFrame; // Use static frame for attack/idle
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

    @Override
    public void attack() {
        // Call base attack to apply damage/cooldown and set ATTACKING state
        super.attack();
        // If base set ATTACKING, override animation duration to the real animation
        // length
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
