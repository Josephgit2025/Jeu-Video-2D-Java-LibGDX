package com.main.entities.enemies;
import com.main.entities.Unit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.map.Base;

public class FZombie extends Zombie {

    public FZombie(int posX, int posY, Base allyBase) {
        super("zombie/normal/Walk1.png", posX, posY, allyBase);
        this.health = (int)(2.0f * Unit.HP_BASE);
        this.speed = 30;
        this.attackDamage = 0.6f * Unit.DAMAGE_BASE;
        this.attackSpeed = 0.8f * Unit.ATTACK_SPEED_BASE;
        this.range = 50;
        
        // Load walk animation
        TextureRegion[] leftFrames = loadFrames("zombie/normal/Walk%d.png", 10);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] attackTex = loadFrames("zombie/normal/Attack%d.png", 4);
        attackAnimation = new Animation<>(FRAME_DURATION, attackTex);
        attackAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // Load idle frame
        Texture idleTex = new Texture(Gdx.files.internal("zombie/normal/Walk1.png"));
        loadedTextures.add(idleTex);
        this.idleFrame = new TextureRegion(idleTex);
    }
}
