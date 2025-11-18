package com.main.entities.units;
import com.main.entities.Unit;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.map.Base;

public class Tank extends Soldier {


    public static final int COST = 40;

    public Tank(float posX, float posY, Base allyBase) {
        super("Tank/Ride1.png", posX, posY, allyBase);
        // Stats calculées
        this.health = (int)(2.0f * Unit.HP_BASE);
        this.attackDamage = 0.3f * Unit.DAMAGE_BASE;
        this.attackSpeed = 0.6f * Unit.ATTACK_SPEED_BASE;
        this.speed = 40;
        this.range = 100;

        // Load walk animation
        TextureRegion[] walkFrames = loadFrames("Tank/Ride%d.png", 2);
        walkAnimation = new Animation<>(FRAME_DURATION, walkFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // Use spritesheet for attack animation (muzzle flash effect)
        TextureRegion[] attackFrames = loadFrames("Tank/Attack%d.png", 7);
        attackAnimation = new Animation<>(FRAME_DURATION, attackFrames);
        // Attack should play once per attack, not loop
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // Idle: reuse walk frames if no dedicated idle frames are provided
        this.idleFrame = walkFrames[0];
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
    protected float getAttackAnimationDuration() {
        if (attackAnimation != null) {
            return attackAnimation.getAnimationDuration();
        }
        return super.getAttackAnimationDuration();
    }

    // @Override
    // public void move(float delta) {
    // // Update attack animation timer
    // if (attackAnimationTimer > 0) {
    // attackAnimationTimer -= delta;
    // this.stateTime += delta;
    // if (attackAnimationTimer <= 0) {
    // currentState = UnitState.WALKING;
    // this.stateTime = 0;
    // }
    // return;
    // }
    // // If there's a living unit target, handle engagement
    // if (target != null && !target.isDead()) {
    // double distance = Math
    // .sqrt(Math.pow(this.posX - target.getPosX(), 2) + Math.pow(this.posY -
    // target.getPosY(), 2));
    // if (distance <= this.range) {
    // // In range: use shared attack logic so damage, cooldown and attack animation
    // // timer are applied
    // if (attackCooldown <= 0f) {
    // attack();
    // this.stateTime = 0f;
    // } else {
    // currentState = UnitState.IDLE;
    // this.stateTime += delta;
    // }
    // return;
    // }
    // }

    // // If attack animation or cooldown stopped us, check generic stop condition
    // if (shouldStopMoving()) {
    // currentState = UnitState.IDLE;
    // this.stateTime += delta;
    // return;
    // }

    // // Default movement: move right (soldier direction) with collision check
    // currentState = UnitState.WALKING;
    // float newX = calculateNewPositionX(delta, 1);
    // this.setSpritePosX(newX);
    // this.stateTime += delta;
    // }
}
