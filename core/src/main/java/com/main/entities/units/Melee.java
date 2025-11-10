package com.main.entities.units;

import java.util.ArrayList;
import java.util.List;

import com.main.entities.player.Ability;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.entities.Unit;

public class Melee extends Soldier {
    private Animation<TextureRegion> walkAnimation;
    private final float frameDuration = 0.2f;
    private float stateTime = 0f;
    private TextureRegion[] res;

    public Melee(float posX, float posY) {
        super("Melee/Walk1.png", posX, posY);
        this.health = 200;
        this.attackDamage = 20;
        this.speed = 100;
        this.attackSpeed = 2;
        this.range = 80;
        this.res = loadFrames("Melee/Walk%d.png", 8);
        this.walkAnimation = new Animation<>(frameDuration, res);
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
    public void render(SpriteBatch batch){
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime,true);
        batch.draw(currentFrame,posX,posY);
    }

    @Override
    public void move(float delta){
        this.setSpritePosX(this.posX + this.speed * delta);
        this.stateTime = this.stateTime + delta;
    }

}