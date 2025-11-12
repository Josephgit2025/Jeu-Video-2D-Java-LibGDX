package com.main.entities.enemies;

import java.util.ArrayList;
import java.util.List;

import com.main.entities.player.Ability;
import com.main.map.WarMap;
import com.main.weapons.Weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.entities.Unit;
import com.main.map.WarMap;
import com.main.weapons.Machette;
import com.main.weapons.Weapon;

public class CZombie extends Zombie {
    private Animation<TextureRegion> walkLeft;
    private TextureRegion attackFrame;
    // uses inherited stateTime from Unit
    private boolean moving = false;
    private List<Texture> loadedTextures = new ArrayList<>();
    private final float FRAME_DURATION = 0.2f;

    public CZombie(int posX, int posY) {
        super("zombie/crawl/Walk1.png", posX, posY);
        this.health = 300; // Augmenté pour être plus résistant
        this.speed = 40;
        this.attackDamage = 15;
        this.attackSpeed = 1.2f; // 1.2 seconds between attacks (slower zombie)
        this.range = 50; // Portée égale au Melee

        TextureRegion[] leftFrames = loadFrames("zombie/crawl/Walk%d.png", 10);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);
        
        // Use first frame as attack pose
        this.attackFrame = leftFrames[0];
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
        // If there's a unit target and it's in range, attack
        if (target != null && !target.isDead()) {
            double distance = Math.sqrt(Math.pow(this.posX - target.getPosX(), 2) + Math.pow(this.posY - target.getPosY(), 2));
            if (distance <= this.range) {
                attack();
                this.stateTime += delta;
                return;
            }
        }

        // If should stop (eg base in range or attack animation), idle
        if (shouldStopMoving()) {
            currentState = UnitState.IDLE;
            this.stateTime += delta;
            return;
        }

        // Default: move left (zombies direction) with collision check
        currentState = UnitState.WALKING;
        float newX = calculateNewPositionX(delta, -1); // -1 for left movement
        this.setSpritePosX(newX);
        this.moving = true;
        this.stateTime += delta;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame;
        
        // Choose frame based on current state
        switch (getCurrentState()) {
            case ATTACKING:
            case IDLE:
                currentFrame = attackFrame;
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
    protected float getAttackAnimationDuration() {
        return FRAME_DURATION;
    }
}
