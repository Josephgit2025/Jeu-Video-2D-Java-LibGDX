package com.main.entities.enemies;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.entities.Unit;


public class WZombie extends Zombie {
    private Animation<TextureRegion> walkLeft;
    private float stateTime = 0f;
    private boolean moving = false;
    private List<Texture> loadedTextures = new ArrayList<>();
    private final float FRAME_DURATION = 0.2f;

    public WZombie(int posX, int posY) {
        super("zombie/women/Walk1.png", posX, posY);
        this.health = 200;
        this.speed = 130;
        this.attackDamage = 10;
        this.attackSpeed = 2;
        this.range = 3;

    TextureRegion[] leftFrames = loadFrames("zombie/women/Walk%d.png", 7);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void move(float delta) {
        this.setSpritePosX(this.posX - this.speed * delta);
        this.moving = true;
        this.stateTime += delta;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = walkLeft.getKeyFrame(stateTime, true);
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
}
