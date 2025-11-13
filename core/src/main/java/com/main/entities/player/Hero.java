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
    
    // Gold system
    protected int gold = 0;
    protected int maxHealth = 500;
    private WarMap map;
    private Animation<TextureRegion> walkRight, walkLeft, walkUp, walkDown;
    // uses inherited stateTime from Unit
    private boolean moving = false;
    private Direction direction = Direction.DOWN;
    private List<Texture> loadedTextures = new ArrayList<>();
    private final float FRAME_DURATION = 0.12f;
    private final float FRAME_DURATIONW = 0.12f;

    public Hero(float posX, float posY, WarMap map) {
        super("sold/Idle.png", posX, posY);

        TextureRegion[] rightFrames = loadFrames("sold/RIght%d.png", 8);
        walkRight = new Animation<>(FRAME_DURATION, rightFrames);
        walkRight.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] leftFrames = loadFrames("sold/Left%d.png",
                8);
        walkLeft = new Animation<>(FRAME_DURATION, leftFrames);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] upFrames = loadFrames("sold/Up%d.png",
                8);
        walkUp = new Animation<>(FRAME_DURATIONW, upFrames);
        walkUp.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] downFrames = loadFrames("sold/Down%d.png",
                8);
        walkDown = new Animation<>(FRAME_DURATIONW, downFrames);
        walkDown.setPlayMode(Animation.PlayMode.LOOP);
        this.health = 500;
        this.weapon = new Machette();
        this.speed = 8;
        this.attackSpeed = 1;
        this.map = map;
        
        // initialiser gold
        this.gold = 50; // Start with 50 gold
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

    public void update(float delta, float mapWidth, float mapHeight, List<Unit> units) {
        // Déplacement avec les touches WASD ou flèches
        moving = false;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight(delta, mapWidth, units);
            moving = true;
            direction = Direction.RIGHT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveUp(delta, mapHeight, units);
            moving = true;
            direction = Direction.UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft(delta, units);
            moving = true;
            direction = Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            moveDown(delta, units);
            moving = true;
            direction = Direction.DOWN;
        }

        if (moving) {
            stateTime += delta;
        } else {
            stateTime = 0f;
        }
    }

    private boolean checkHeroEnemyCollisions(float newX, float newY, Unit enemy) {
        if (enemy != null){
            if (enemy.isDead()) 
                return false;
            
            // Simple collision detection using bounding boxes
            float enemyX = enemy.getPosX();
            float enemyY = enemy.getPosY();
            
            // Calculate distance between hero and enemy
            float dx = newX - enemyX;
            float dy = newY - enemyY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            // System.out.println("Distanche with " + enemy.getClass().getSimpleName() + " " + distance);
            if (distance < 50f) {
                System.out.println("Hero hard stuck in Zombie");
                return true;
            }
            return false;
        }
        return false;
    }

    public void moveUp(float delta, float mapHeight, List<Unit> enemies) {
        float newY = this.getPosY() + speed * delta * 60; // delta pour smooth movement
        if (!map.isCollisionRect(this.posX, newY, this.width, this.height) && !checkHeroEnemyCollisions(this.posX, newY, this.findClosestEnemy(enemies))) {
            if (newY + this.height > mapHeight) {
                this.setSpritePosY(mapHeight - this.height);
            } else {
                this.setSpritePosY(newY);
            }
        }
    }

    public void moveDown(float delta, List<Unit> enemies) {
        float newY = this.getPosY() - speed * delta * 60;
        if (!map.isCollisionRect(this.posX, newY, this.width, this.height) && !checkHeroEnemyCollisions(this.posX, newY, this.findClosestEnemy(enemies))) {
            if (newY < 0) {
                this.setSpritePosY(0);
            } else {
                this.setSpritePosY(newY);
            }
        }
    }

    public void moveLeft(float delta, List<Unit> enemies) {
        float newX = this.getPosX() - speed * delta * 60;
        if (!map.isCollisionRect(newX, this.posY, this.width, this.height) && !checkHeroEnemyCollisions(newX, this.posY, this.findClosestEnemy(enemies))) {
            if (newX < 0) {
                this.setSpritePosX(0);
            } else {
                this.setSpritePosX(newX);
            }
        }
    }

    public void moveRight(float delta, float mapWidth, List<Unit> enemies) {
        float newX = this.getPosX() + speed * delta * 60;
        if (!map.isCollisionRect(newX, this.posY, this.width, this.height) && !checkHeroEnemyCollisions(newX, this.posY, this.findClosestEnemy(enemies))) {
            if (newX + this.width > mapWidth) {
                this.setSpritePosX(mapWidth - this.width);
            } else {
                this.setSpritePosX(newX);
            }
        }
    }

    public void setWeapon(Weapon weapon) {
        if (weapon != null) {
            this.weapon = weapon;
        }
    }

    @Override
    public void attack() {
        if (target == null || target.isDead()) {
            return;
        }
        if (attackCooldown <= 0 && weapon != null) {
            if (weapon.getMunitions() > 0 || weapon.getMaxMunitions() == -1) {
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
        float visualWidth;
        float visualHeight;

        switch (direction) {
            case RIGHT:
                currentFrame = walkRight.getKeyFrame(stateTime, true);
                visualWidth = 50;
                visualHeight = 50;
                break;
            case LEFT:
                currentFrame = walkLeft.getKeyFrame(stateTime, true);
                visualWidth = 50;
                visualHeight = 50;
                break;
            case UP:
                currentFrame = walkUp.getKeyFrame(stateTime, true);
                visualWidth = 30;
                visualHeight = 50;
                break;
            default:
                currentFrame = walkDown.getKeyFrame(stateTime, true);
                visualWidth = 30;
                visualHeight = 50;
                break;
        }

        // Dessiner le sprite plus grand visuellement (90x90) mais hitbox reste 32x48

        // Centrer horizontalement et aligner les pieds du sprite avec le bas de la
        // hitbox
        float offsetX = (this.width - visualWidth) / 2;
        float offsetY = 0; // Aligner le bas du sprite avec le bas de la hitbox (pieds alignés)

        batch.draw(currentFrame, this.posX + offsetX, this.posY + offsetY, visualWidth, visualHeight);
    }

    @Override
    public void move(float delta) {
        // Hero movement is handled via specific methods (moveUp/moveDown/etc.)
        // This default implementation does nothing.
    }

    // === HEALTH SYSTEM ===
    
    /**
     * Get current health
     * @return Current health value
     */
    public int getCurrentHealth() {
        return this.health;
    }
    
    /**
     * Get maximum health
     * @return Maximum health value
     */
    public int getMaxHealth() {
        return this.maxHealth;
    }
    
    /**
     * Set maximum health
     * @param maxHealth New maximum health
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        if (this.health > maxHealth) {
            this.health = maxHealth;
        }
    }
    
    /**
     * Heal the hero
     * @param amount Amount to heal
     */
    public void heal(int amount) {
        this.health = Math.min(maxHealth, this.health + amount);
    }
    
    // === GOLD SYSTEM ===
    
    /**
     * Get current gold amount
     * @return Current gold
     */
    public int getGold() {
        return this.gold;
    }
    
    /**
     * Set gold amount directly
     * @param gold Gold amount to set
     */
    public void setGold(int gold) {
        this.gold = Math.max(0, gold);
    }
    
    /**
     * Add gold to current amount
     * @param amount Amount to add
     */
    public void addGold(int amount) {
        this.gold += amount;
    }
    
    /**
     * Remove gold from current amount
     * @param amount Amount to remove
     * @return true if successful, false if not enough gold
     */
    public boolean removeGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }

}