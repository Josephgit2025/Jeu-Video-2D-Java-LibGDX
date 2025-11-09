package com.main.entities.player;

import java.util.ArrayList;
import java.util.List;

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

public class Hero extends Unit {

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    protected int xp;
    protected int level;
    protected List<Ability> abilities = new ArrayList<>();
    protected int strength;
    protected int dexterity;
    protected int agility;
    protected Weapon weapon;
    private WarMap map;
    private Animation<TextureRegion> walkRight, walkLeft, walkUp, walkDown;
    private float stateTime = 0f;
    private boolean moving = false;
    private Direction direction = Direction.DOWN;
    private List<Texture> loadedTextures = new ArrayList<>();
    private final float FRAME_DURATION = 0.08f;
    private final float FRAME_DURATIONW = 0.15f;

    public Hero(float posX, float posY, WarMap map) {
        super("units/hero/down1n.png", posX, posY);

        TextureRegion[] rightFrames = loadFrames("units/hero/right%d.png", 8);
        walkRight = new Animation<>(FRAME_DURATION, rightFrames);
        walkRight.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] leftFrames = loadFrames("units/hero/left%d.png",
                8);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] upFrames = loadFrames("units/hero/up%dn.png",
                4);
        walkUp = new Animation<>(FRAME_DURATIONW, upFrames);
        walkUp.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] downFrames = loadFrames("units/hero/down%dn.png",
                3);
        walkDown = new Animation<>(FRAME_DURATIONW, downFrames);
        walkDown.setPlayMode(Animation.PlayMode.LOOP);
        this.health = 500;
        this.weapon = new Machette();
        this.speed = 8;
        this.attackSpeed = 1;
        this.map = map;
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

    /**
     * Met à jour le Hero (déplacements avec LibGDX)
     * 
     * @param delta     Le temps écoulé depuis la dernière frame
     * @param mapWidth  Largeur de la map en pixels
     * @param mapHeight Hauteur de la map en pixels
     */

    public void update(float delta, float mapWidth, float mapHeight) {
        // Déplacement avec les touches WASD ou flèches
        moving = false;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight(delta, mapWidth);
            moving = true;
            direction = Direction.RIGHT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveUp(delta, mapHeight);
            moving = true;
            direction = Direction.UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft(delta);
            moving = true;
            direction = Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            moveDown(delta);
            moving = true;
            direction = Direction.DOWN;
        }

        if (moving) {
            stateTime += delta;
        } else {
            stateTime = 0f;
        }
    }

    private void moveUp(float delta, float mapHeight) {
        float newY = this.getPosY() + speed * delta * 60; // delta pour smooth movement
        if (!map.isCollisionRect(this.posX, newY, this.width, this.height)){
            if (newY + this.height > mapHeight) {
                this.setSpritePosY(mapHeight - this.height);
            } else {
                this.setSpritePosY(newY);
            }
        }
    }

    public void moveDown(float delta) {
        float newY = this.getPosY() - speed * delta * 60;
        if (!map.isCollisionRect(this.posX, newY, this.width, this.height)) {
            if (newY < 0) {
                this.posY = 0;
            } else {
                this.posY = newY;
            }
        }
    }

    public void moveLeft(float delta) {
        float newX = this.getPosX() - speed * delta * 60;
        if (!map.isCollisionRect(newX, this.posY, this.width, this.height)) {
            if (newX < 0) {
                this.posX = 0;
            } else {
                this.posX = newX;
            }
        }
    }

    public void moveRight(float delta, float mapWidth) {
        float newX = this.getPosX() + speed * delta * 60;
        if (!map.isCollisionRect(newX, this.posY, this.width, this.height)) {
            if (newX + this.width > mapWidth) {
                this.posX = mapWidth - this.width;
            } else {
                this.posX = newX;
            }
        }
    }

    @Override
    public void attack() {
        if (target == null || target.isDead()) {
            return;
        }
        if (attackCooldown <= 0 && weapon != null) {
            if (weapon.getMunitions() > 0) {
                weapon.attack();
                int totalDamage = weapon.getDamage();
                target.takeDamage(totalDamage);
                attackCooldown = weapon.getAttackSpeed();
            } else {
                weapon.reload();
            }
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame;
        switch (direction) {
            case RIGHT:
                currentFrame = walkRight.getKeyFrame(stateTime, true);
                break;
            case LEFT:
                currentFrame = walkLeft.getKeyFrame(stateTime, true);
                break;
            case UP:
                currentFrame = walkUp.getKeyFrame(stateTime, true);
                break;
            default:
                currentFrame = walkDown.getKeyFrame(stateTime, true);
                break;
        }
        batch.draw(currentFrame, this.posX, this.posY);
    }

    public void move() {
    }

}