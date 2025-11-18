package com.main.entities.enemies;
import com.main.entities.Unit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.map.Base;

public class CZombie extends Zombie {

    public CZombie(int posX, int posY, Base allyBase) {
        super("zombie/crawl/Walk1.png", posX, posY, allyBase);
        this.health = (int)(0.7f * Unit.HP_BASE);
        this.speed = 60;
        this.attackDamage = 1.3f * Unit.DAMAGE_BASE;
        this.attackSpeed = 0.5f * Unit.ATTACK_SPEED_BASE;
        this.range = 50;

        // Load walk animation
        TextureRegion[] leftFrames = loadFrames("zombie/crawl/Walk%d.png", 10);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] attackTex = loadFrames("zombie/crawl/Attack%d.png", 12);
        attackAnimation = new Animation<>(FRAME_DURATION, attackTex);
        attackAnimation.setPlayMode(Animation.PlayMode.LOOP);

        Texture idleTex = new Texture(Gdx.files.internal("zombie/crawl/Walk1.png"));
        this.idleFrame = new TextureRegion(idleTex);
    }
}
