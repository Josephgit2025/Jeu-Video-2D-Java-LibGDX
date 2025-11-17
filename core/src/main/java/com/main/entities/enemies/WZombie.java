package com.main.entities.enemies;
import com.main.entities.Unit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.map.Base;

public class WZombie extends Zombie {

    public WZombie(int posX, int posY, Base allyBase) {
        super("zombie/women/Walk1.png", posX, posY, allyBase);
        this.health = (int)(0.6f * Unit.HP_BASE);
        this.speed = 60;
        this.attackDamage = 0.9f * Unit.DAMAGE_BASE;
        this.attackSpeed = 1.2f * Unit.ATTACK_SPEED_BASE;
        this.range = 50;
        
        // Load walk animation
        TextureRegion[] leftFrames = loadFrames("zombie/women/Walk%d.png", 7);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] attackTex = loadFrames("zombie/women/Attack%d.png", 20);
        attackAnimation = new Animation<>(0.1f, attackTex);
        // Attack should play once when triggered
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        Texture idleTex = new Texture(Gdx.files.internal("zombie/women/Walk1.png"));
        this.idleFrame = new TextureRegion(idleTex);
    }
}