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
    private Animation<TextureRegion> attackAnimation;
    private TextureRegion idleFrame;
    // uses inherited stateTime from Unit
    private boolean moving = false;
    private List<Texture> loadedTextures = new ArrayList<>();
    private final float FRAME_DURATION = 0.2f;

    public FZombie(int posX, int posY) {
        super("zombie/normal/Walk1.png", posX, posY);
        this.health = 450; // Augmenté pour être plus résistant
        this.speed = 80;
        this.attackDamage = 25;
        this.attackSpeed = 0.8f; // 0.8 seconds between attacks (fast zombie)
        this.range = 50; // Portée égale au Melee
        
        // Load walk animation
        TextureRegion[] leftFrames = loadFrames("zombie/normal/Walk%d.png", 10);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);
        
        // Load attack animation
        Texture attackTex = new Texture(Gdx.files.internal("zombie/normal/Attack.png"));
        this.attackAnimation = new Animation<>(0.2f, new TextureRegion(attackTex));
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        
        // Load idle frame
        Texture idleTex = new Texture(Gdx.files.internal("zombie/normal/Idle.png"));
        this.idleFrame = new TextureRegion(idleTex);
    }

    @Override
    public void move(float delta) {
        this.moving = false; // Reset moving state
        
        // Update attack animation timer
        if (attackAnimationTimer > 0) {
            attackAnimationTimer -= delta;
            this.stateTime += delta;
            if (attackAnimationTimer <= 0) {
                currentState = UnitState.WALKING;
                this.stateTime = 0;
            }
            return;
        }
        
        // Vérifie si une cible est à portée, si oui, attaque (utilise Unit.attack() pour gérer cooldowns/timers)
        if (target != null && !target.isDead()) {
            double distance = Math.sqrt(Math.pow(this.posX - target.getPosX(), 2) + Math.pow(this.posY - target.getPosY(), 2));
            if (distance <= this.range) {
                // Use the shared attack logic so damage, cooldown and attack animation timer are applied
                attack();
                this.stateTime += delta;
                return;
            }
        }
        
        // Only move and animate if not in combat
        currentState = UnitState.WALKING;
        this.setSpritePosX(this.posX - this.speed * delta);
        this.moving = true;
        this.stateTime += delta;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame;
        
        // Choose animation based on current state
        switch (getCurrentState()) {
            case ATTACKING:
                currentFrame = attackAnimation.getKeyFrame(stateTime, false);
                break;
            case IDLE:
                currentFrame = idleFrame;
                break;
            case WALKING:
            default:
                currentFrame = walkLeft.getKeyFrame(stateTime, true);
                break;
        }
        
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

    @Override
    protected float getAttackAnimationDuration() {
        return attackAnimation.getAnimationDuration();
    }

}
