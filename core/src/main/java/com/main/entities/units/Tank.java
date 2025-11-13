package com.main.entities.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tank extends Soldier {

    public Tank(float posX, float posY) {
        super("Tank/Ride1.png", posX, posY);
        this.health = 500;
        this.attackDamage = 30;
        this.speed = 15;
        this.attackSpeed = 3.0f; // 3 seconds between attacks (slow heavy weapon)
        this.range = 100; // Portée moyenne

        // Load walk animation
        TextureRegion[] walkFrames = loadFrames("Tank/Ride%d.png", 2);
        walkAnimation = new Animation<>(FRAME_DURATION, walkFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // Use spritesheet for attack animation (muzzle flash effect)
        Texture attackSheet = new Texture(Gdx.files.internal("Tank/right_fire_blue-Sheet.png"));
        loadedTextures.add(attackSheet);
        TextureRegion[][] tmp = TextureRegion.split(attackSheet,
                attackSheet.getWidth() / 3,
                attackSheet.getHeight() / 1);
        TextureRegion[] attackFrames = new TextureRegion[3];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                attackFrames[index++] = tmp[i][j];
            }
        }
        attackAnimation = new Animation<>(0.1f, attackFrames);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // Idle is just first frame of walk
        this.idleFrame = walkFrames[0];
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = getCurrentFrame();
        if (currentFrame != null) {
            float visualWidth = 85;
            float visualHeight = 50;
            float offsetX = (this.width - visualWidth) / 2;
            float offsetY = 0;
            
            batch.draw(currentFrame, this.posX + offsetX, this.posY + offsetY, visualWidth, visualHeight);
        }
    }
}
 
