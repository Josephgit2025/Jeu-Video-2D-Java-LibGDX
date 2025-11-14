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
import com.main.weapons.SniperRifle;
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
        this.weapon = new SniperRifle();
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
        // Cache closest enemy calculation to avoid 4 calls per frame
        Unit closestEnemy = this.findClosestEnemy(units);

        // Auto-ciblage : si la cible actuelle est morte ou hors de portée, trouver une nouvelle cible
        if (target == null || target.isDead()) {
            // Chercher l'ennemi le plus proche dans la portée
            Unit enemyInRange = findClosestEnemyInRange(units, weapon.getRange());
            if (enemyInRange != null) {
                this.setTarget(enemyInRange);
            }
        }

        // Attaque avec la touche ESPACE (maintenue ou appuyée)
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            // Si pas de cible ou cible morte, chercher une nouvelle cible
            if (target == null || target.isDead()) {
                Unit enemyInRange = findClosestEnemyInRange(units, weapon.getRange());
                if (enemyInRange != null) {
                    System.out.println("New target acquired: " + enemyInRange.getClass().getSimpleName());
                    this.setTarget(enemyInRange);
                }
            }
            // Attaquer si on a une cible valide
            if (target != null && !target.isDead()) {
                this.attack();
            }
        }
        this.updateCooldown(delta);

        // Déplacement avec les touches WASD ou flèches
        moving = false;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            tryMove(speed * delta * 60, 0, mapWidth, mapHeight, closestEnemy);
            moving = true;
            direction = Direction.RIGHT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            tryMove(0, speed * delta * 60, mapWidth, mapHeight, closestEnemy);
            moving = true;
            direction = Direction.UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            tryMove(-speed * delta * 60, 0, mapWidth, mapHeight, closestEnemy);
            moving = true;
            direction = Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            tryMove(0, -speed * delta * 60, mapWidth, mapHeight, closestEnemy);
            moving = true;
            direction = Direction.DOWN;
        }

        if (moving) {
            stateTime += delta;
        } else {
            stateTime = 0f;
        }
    }

    /**
     * Unified movement method - replaces moveUp/Down/Left/Right
     * 
     * @param deltaX       X velocity
     * @param deltaY       Y velocity
     * @param mapWidth     Map width boundary
     * @param mapHeight    Map height boundary
     * @param closestEnemy Cached closest enemy for collision
     */
    private void tryMove(float deltaX, float deltaY, float mapWidth, float mapHeight, Unit closestEnemy) {
        float newX = this.posX + deltaX;
        float newY = this.posY + deltaY;

        // Check map collision and enemy collision
        if (!map.isCollisionRect(newX, newY, this.width, this.height) &&
                !checkHeroEnemyCollisions(newX, newY, closestEnemy)) {

            // Apply boundaries and set position
            newX = Math.max(0, Math.min(newX, mapWidth - this.width));
            newY = Math.max(0, Math.min(newY, mapHeight - this.height));

            this.setSpritePosX(newX);
            this.setSpritePosY(newY);
        }
    }

    private boolean checkHeroEnemyCollisions(float newX, float newY, Unit enemy) {
        if (enemy == null || enemy.isDead()) {
            return false;
        }

        // Calculate distance between hero and enemy
        float dx = newX - enemy.getPosX();
        float dy = newY - enemy.getPosY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < 50f) {
            return true;
        }
        return false;
    }

    /**
     * Find the closest enemy within attack range
     * @param units List of all enemy units
     * @param range Attack range
     * @return Closest enemy in range, or null if none found
     */
    private Unit findClosestEnemyInRange(List<Unit> units, int range) {
        Unit closest = null;
        double minDistance = range;
        
        for (Unit unit : units) {
            if (unit.isDead()) continue;
            
            double distance = Math.sqrt(
                Math.pow(this.posX - unit.getPosX(), 2) +
                Math.pow(this.posY - unit.getPosY(), 2)
            );
            
            if (distance <= range && distance < minDistance) {
                minDistance = distance;
                closest = unit;
            }
        }
        
        return closest;
    }

    // Keep public methods for backward compatibility
    public void moveUp(float delta, float mapHeight, List<Unit> enemies) {
        tryMove(0, speed * delta * 60, Float.MAX_VALUE, mapHeight, findClosestEnemy(enemies));
    }

    public void moveDown(float delta, List<Unit> enemies) {
        tryMove(0, -speed * delta * 60, Float.MAX_VALUE, Float.MAX_VALUE, findClosestEnemy(enemies));
    }

    public void moveLeft(float delta, List<Unit> enemies) {
        tryMove(-speed * delta * 60, 0, Float.MAX_VALUE, Float.MAX_VALUE, findClosestEnemy(enemies));
    }

    public void moveRight(float delta, float mapWidth, List<Unit> enemies) {
        tryMove(speed * delta * 60, 0, mapWidth, Float.MAX_VALUE, findClosestEnemy(enemies));
    }

    public void setWeapon(Weapon weapon) {
        if (weapon != null) {
            this.weapon = weapon;
        }
    }

    @Override
    public void attack() {
        if (target == null || target.isDead()) {
            System.out.print("Target null");
            return;
        }
        double distance = Math.sqrt(Math.pow(this.posX - target.getPosX(), 2) +
                Math.pow(this.posY - target.getPosY(), 2));
        if (distance > weapon.getRange()) {
            System.out.println("Out of range");
            return;
        }
        if (attackCooldown <= 0 && weapon != null) {
            if (weapon.getMunitions() > 0 || weapon.getMaxMunitions() == -1) {
                weapon.attack();
                int totalDamage = weapon.getDamage();
                System.out.println("Hero attacks " + target.getClass().getSimpleName() + " for " + totalDamage + " damage");
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
     * 
     * @return Current health value
     */
    public int getCurrentHealth() {
        return this.health;
    }

    /**
     * Get maximum health
     * 
     * @return Maximum health value
     */
    public int getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Set maximum health
     * 
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
     * 
     * @param amount Amount to heal
     */
    public void heal(int amount) {
        this.health = Math.min(maxHealth, this.health + amount);
    }

    // === GOLD SYSTEM ===

    /**
     * Get current gold amount
     * 
     * @return Current gold
     */
    public int getGold() {
        return this.gold;
    }

    /**
     * Set gold amount directly
     * 
     * @param gold Gold amount to set
     */
    public void setGold(int gold) {
        this.gold = Math.max(0, gold);
    }

    /**
     * Add gold to current amount
     * 
     * @param amount Amount to add
     */
    public void addGold(int amount) {
        this.gold += amount;
    }

    /**
     * Remove gold from current amount
     * 
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