package com.main.entities.units;
import com.main.entities.Unit;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.map.Base;

public class Sniper extends Soldier {
        protected int cost;


    public static final int COST = 1;

    public Sniper(int posX, int posY, Base allyBase) {
        super("Sniper/Walk1.png", posX, posY, allyBase);
        // Stats calculées
        this.health = (int)(0.5f * Unit.HP_BASE);
        this.attackDamage = 1.5f * Unit.DAMAGE_BASE;
        this.attackSpeed = 2f * Unit.ATTACK_SPEED_BASE;
        this.cost = 40;
        this.speed = 30;
        this.range = 150;

        // Load walk animation
        TextureRegion[] walkFrames = loadFrames("Sniper/Walk%d.png", 8);
        this.walkAnimation = new Animation<>(FRAME_DURATION, walkFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // Load attack animation
        TextureRegion[] attackFrames = loadFrames("Sniper/Attack%d.png", 7);
        this.attackAnimation = new Animation<>(0.1f, attackFrames);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // Texture idleTex = new Texture(Gdx.files.internal("Sniper/Idle.png"));
        // this.idleFrame = new TextureRegion(idleTex);

        TextureRegion[] idleFrames = loadFrames("Sniper/Idle%d.png", 8);
        this.idleFramer = new Animation<>(FRAME_DURATION, idleFrames);
        idleFramer.setPlayMode(Animation.PlayMode.LOOP);
    }
}
