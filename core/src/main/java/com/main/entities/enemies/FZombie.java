package com.main.entities.enemies;

import java.util.ArrayList;
import java.util.List;

import com.main.entities.player.Ability;
import com.main.map.WarMap;
import com.main.weapons.Weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.entities.Unit;
import com.main.map.WarMap;
import com.main.weapons.Machette;
import com.main.weapons.Weapon;



public class FZombie extends Zombie {
    private Animation<TextureRegion> walkLeft;
    private float stateTime = 0f;
    private boolean moving = false;
    private List<Texture> loadedTextures = new ArrayList<>();
    private final float FRAME_DURATION = 0.2f;

    public FZombie(int posX, int posY) {
        super("zombie/normal/Walk1.png", posX, posY);
        this.health = 500;
        this.speed = 80;
        this.attackDamage = 20; 
        this.attackSpeed = 3;
        this.range = 2;

        TextureRegion[] leftFrames = loadFrames("zombie/normal/Walk%d.png", 10);
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
