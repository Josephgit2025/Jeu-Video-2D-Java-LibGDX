package com.main.entities.units;
import com.main.entities.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.map.Base;

public class Melee extends Soldier {
        protected int cost;


    public static final int COST = 1;

    public Melee(float posX, float posY, Base allyBase) {
        super("Melee/Walk1.png", posX, posY, allyBase);
        // Stats calculées
        this.health = (int)(0.7f * Unit.HP_BASE);
        this.attackDamage = 0.5f * Unit.DAMAGE_BASE;
        this.attackSpeed = 0.8f * Unit.ATTACK_SPEED_BASE;
        this.cost = 30;
        this.speed = 60;
        this.range = 50;

        // Load walk animation
        TextureRegion[] walkFrames = loadFrames("Melee/Walk%d.png", 8);
        this.walkAnimation = new Animation<>(FRAME_DURATION, walkFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // Load attack animation
        TextureRegion[] attackFrames = loadFrames("Melee/Attack_%d.png", 10);
        this.attackAnimation = new Animation<>(0.15f, attackFrames);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // Load idle frame
        Texture idleTex = new Texture(Gdx.files.internal("Melee/Walk1.png"));
        loadedTextures.add(idleTex);
        this.idleFrame = new TextureRegion(idleTex);
    }

    @Override
    protected float getAttackAnimationDuration() {
        if (this.attackAnimation != null) {
            return this.attackAnimation.getAnimationDuration();
        }
        return super.getAttackAnimationDuration();
    }
}
