package com.main.entities.units;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.entities.Unit;

public class Tank extends Soldier {
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> attackAnimation;
    private TextureRegion idleFrame;
    private float stateTime = 0f;
    private boolean moving = false;
    private List<Texture> loadedTextures = new ArrayList<>();
    private final float FRAME_DURATION = 0.2f;

    public Tank(float posX, float posY) {
        super("Tank/Ride1.png", posX, posY);
        this.health = 500;
        this.attackDamage = 30;
        this.speed = 15;
        this.attackSpeed = 3.0f; // 3 seconds between attacks (slow heavy weapon)
        this.range = 100; // Portée moyenne

        // Load walk animation
        TextureRegion[] walkFrames = loadFrames("Tank/Ride%d.png", 2);
        walkAnimation = new Animation<>(FRAME_DURATION, walkFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // Use spritesheet for attack animation (muzzle flash effect)
        Texture attackSheet = new Texture(Gdx.files.internal("Tank/right_fire_blue-Sheet.png"));
        TextureRegion[][] tmp = TextureRegion.split(attackSheet,
                attackSheet.getWidth() / 3,
                attackSheet.getHeight() / 1);
        TextureRegion[] attackFrames = new TextureRegion[3];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                attackFrames[index++] = tmp[i][j];
            }
        }
        attackAnimation = new Animation<>(0.1f, attackFrames);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // Idle is just first frame of walk
        this.idleFrame = walkFrames[0];
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

        float visualWidth = 85;
        float visualHeight = 50;

        float offsetX = (this.width - visualWidth) / 2;
        float offsetY = 0;

        batch.draw(currentFrame, this.posX + offsetX, this.posY + offsetY, visualWidth, visualHeight);
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
        float newX = calculateNewPositionX(delta, 1);
        this.setSpritePosX(newX);
        this.stateTime += delta;
    }
}