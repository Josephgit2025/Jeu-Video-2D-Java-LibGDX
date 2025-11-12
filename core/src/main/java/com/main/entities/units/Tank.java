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
    private float stateTime = 0f;
    private boolean moving = false;
    private List<Texture> loadedTextures = new ArrayList<>();
    private final float FRAME_DURATION = 0.2f;

    public Tank(float posX, float posY) {
        super("Tank/Ride1.png", posX, posY);
        this.health = 500;
        this.attackDamage = 30;
        this.speed = 15;
        this.attackSpeed = 3;
        this.range = 100;
        TextureRegion[] leftFrames = loadFrames("Tank/Ride%d.png", 2);
        walkAnimation = new Animation<>(FRAME_DURATION, leftFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
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
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        
        float visualWidth = 85;
        float visualHeight = 50;
        
        // Centrer horizontalement et aligner les pieds du sprite avec le bas de la hitbox
        float offsetX = (this.width - visualWidth) / 2;
        float offsetY = 0; // Aligner le bas du sprite avec le bas de la hitbox (pieds alignés)
        
        batch.draw(currentFrame, this.posX + offsetX, this.posY + offsetY, visualWidth, visualHeight);
    }

    @Override
    public void move(float delta) {
        this.setSpritePosX(this.posX + this.speed * delta);
        this.stateTime = this.stateTime + delta;
    }
}