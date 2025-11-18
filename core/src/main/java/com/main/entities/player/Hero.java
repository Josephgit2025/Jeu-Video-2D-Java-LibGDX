package com.main.entities.player;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.main.entities.Unit;
import com.main.map.Base;
import com.main.map.WarMap;
import com.main.weapons.SniperRifle;
import com.main.weapons.AssaultRifle;
import com.main.weapons.Pistol;
import com.main.weapons.SMG;
import com.main.weapons.Shotgun;
import com.main.weapons.Weapon;

public class Hero extends Unit {

    private enum Direction {
        UP, DOWN, LEFT, RIGHT,
        UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT,
        ATTACKUP, ATTACKDOWN, ATTACKLEFT, ATTACKRIGHT
    }

    protected float getCurrentAttackAnimationDuration() {
        switch (direction) {
            case ATTACKRIGHT:
                return (AttackRight != null) ? AttackRight.getAnimationDuration() : 0f;
            case ATTACKLEFT:
                return (AttackLeft != null) ? AttackLeft.getAnimationDuration() : 0f;
            case ATTACKUP:
                return (AttackUp != null) ? AttackUp.getAnimationDuration() : 0f;
            case ATTACKDOWN:
                return (AttackDown != null) ? AttackDown.getAnimationDuration() : 0f;
            default:
                return 0f;
        }
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
    private Animation<TextureRegion> walkUR, walkUL, walkDR, walkDL;
    private TextureRegion idle, idleR, idleL, idleU, idleD, idleUR, idleUL, idleDR, idleDL;
    private Animation<TextureRegion> AttackRight, AttackLeft, AttackUp, AttackDown;

    // uses inherited stateTime from Unit
    private boolean moving = false;
    private Direction direction = Direction.DOWN;
    // previous facing (non-attack) to revert to after attack animation finishes
    private Direction prevDirection = Direction.DOWN;
    private List<Texture> loadedTextures = new ArrayList<>();
    private final float FRAME_DURATION = 0.12f;
    private final float FRAME_DURATIONW = 0.12f;
    private float retargetTimer = 0;
    private final float retargetInterval = 0.1f; // 100ms

    // Audio
    private Sound shootSound;

    public Hero(float posX, float posY, WarMap map, Base allyBase) {
        super("sold/Idle.png", posX, posY);
        this.allyBase = allyBase;

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

        // Diagonal walk animations (if provided in assets)
        TextureRegion[] urFrames = loadFrames("sold/WalkUR%d.png", 8);
        walkUR = new Animation<>(FRAME_DURATION, urFrames);
        walkUR.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] ulFrames = loadFrames("sold/WalkUL%d.png", 8);
        walkUL = new Animation<>(FRAME_DURATION, ulFrames);
        walkUL.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] drFrames = loadFrames("sold/WalkDR%d.png", 8);
        walkDR = new Animation<>(FRAME_DURATION, drFrames);
        walkDR.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] dlFrames = loadFrames("sold/WalkDL%d.png", 8);
        walkDL = new Animation<>(FRAME_DURATION, dlFrames);
        walkDL.setPlayMode(Animation.PlayMode.LOOP);

        // Load single-frame idle textures (cardinal + diagonal)
        idle = loadSingle("sold/Idle.png");
        idleR = loadSingle("sold/IdleR.png");
        idleL = loadSingle("sold/IdleL.png");
        idleU = loadSingle("sold/IdleU.png");
        idleD = loadSingle("sold/IdleD.png");
        idleUR = loadSingle("sold/IdleUR.png");
        idleUL = loadSingle("sold/IdleUL.png");
        idleDR = loadSingle("sold/IdleDR.png");
        idleDL = loadSingle("sold/IdleDL.png");

        TextureRegion[] rightAttack = loadFrames("sold/AttackR%d.png", 4);
        AttackRight = new Animation<>(FRAME_DURATION, rightAttack);
        AttackRight.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[] leftAttack = loadFrames("sold/AttackL%d.png",
                4);
        AttackLeft = new Animation<>(FRAME_DURATION, leftAttack);
        AttackLeft.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[] upAttack = loadFrames("sold/AttackU%d.png",
                4);
        AttackUp = new Animation<>(FRAME_DURATIONW, upAttack);
        AttackUp.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[] downAttack = loadFrames("sold/AttackD%d.png",
                4);
        AttackDown = new Animation<>(FRAME_DURATIONW, downAttack);
        AttackDown.setPlayMode(Animation.PlayMode.NORMAL);

        this.health = 500;
        this.weapon = new Shotgun();
        this.speed = 8;
        this.attackSpeed = 1;
        this.map = map;

        // initialiser gold
        this.gold = 100; // Start with 100 gold
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

    private TextureRegion loadSingle(String path) {
        Texture tex = new Texture(Gdx.files.internal(path));
        loadedTextures.add(tex);
        return new TextureRegion(tex);
    }
    
    @lombok.Generated
    /**
     * Met à jour le Hero (déplacements avec LibGDX)
     * 
     * @param delta     Le temps écoulé depuis la dernière frame
     * @param mapWidth  Largeur de la map en pixels
     * @param mapHeight Hauteur de la map en pixels
     */
    public void update(float delta, float mapWidth, float mapHeight, List<Unit> units) {

        // --- RETARGET SYSTEM (toutes les 100 ms) ---
        retargetTimer += delta;
        if (retargetTimer >= retargetInterval) {
            retargetTimer = 0;

            Unit closest = findClosestEnemy(units);

            // Si pas de cible, ou morte, ou qu’un autre ennemi est plus proche → switch
            if (target == null || target.isDead() ||
                    (closest != null && calculateDistance(closest) < calculateDistance(target))) {

                target = closest;
            }
        }

        // --- ATTAQUE ---
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (target != null && !target.isDead()) {
                this.attack();
            }
        }
        this.updateCooldown(delta);

        // Changing weapon
        // Pistol
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            if (this.gold >= 50 && !(this.weapon instanceof Pistol)) {
                this.weapon = new Pistol();
                this.removeGold(50);
                System.out.println("Changed weapon to Pistol : " + this.weapon.getDamage() + " damage, "
                        + this.weapon.getAttackSpeed() + " attacks/sec, " + this.weapon.getRange() + " range, "
                        + this.weapon.getMaxMunitions() + " munitions.");
            } else if (this.weapon instanceof Pistol) {
                System.out.println("You already have a Pistol.");
            } else {
                System.out.println("Not enough gold : 50 gold required to buy a Pistol -> You only have " + this.gold);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if (this.gold >= 70 && !(this.weapon instanceof Shotgun)) {
                this.weapon = new Shotgun();
                this.removeGold(70);
                System.out.println("Changed weapon to Shotgun : " + this.weapon.getDamage() + " damage, "
                        + this.weapon.getAttackSpeed() + " attacks/sec, " + this.weapon.getRange() + " range, "
                        + this.weapon.getMaxMunitions() + " munitions.");
            } else if (this.weapon instanceof Shotgun) {
                System.out.println("You already have a Shotgun.");
            } else {
                System.out.println("Not enough gold : 70 gold required to buy a Shotgun -> You only have " + this.gold);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            if (this.gold >= 100 && !(this.weapon instanceof SMG)) {
                this.weapon = new SMG();
                this.removeGold(100);
                System.out.println("Changed weapon to SMG : " + this.weapon.getDamage() + " damage, "
                        + this.weapon.getAttackSpeed() + " attacks/sec, " + this.weapon.getRange() + " range, "
                        + this.weapon.getMaxMunitions() + " munitions.");
            } else if (this.weapon instanceof SMG) {
                System.out.println("You already have a SMG.");
            } else {
                System.out.println("Not enough gold : 100 gold required to buy a SMG -> You only have " + this.gold);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            if (this.gold >= 150 && !(this.weapon instanceof AssaultRifle)) {
                this.weapon = new AssaultRifle();
                this.removeGold(150);
                System.out.println("Changed weapon to Assault Rifle : " + this.weapon.getDamage() + " damage, "
                        + this.weapon.getAttackSpeed() + " attacks/sec, " + this.weapon.getRange() + " range, "
                        + this.weapon.getMaxMunitions() + " munitions.");
            } else if (this.weapon instanceof AssaultRifle) {
                System.out.println("You already have a Assault Rifle.");
            } else {
                System.out.println(
                        "Not enough gold : 150 gold required to buy an Assault Rifle -> You only have " + this.gold);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            if (this.gold >= 200 && !(this.weapon instanceof SniperRifle)) {
                this.weapon = new SniperRifle();
                this.removeGold(200);
                System.out.println("Changed weapon to Sniper Rifle : " + this.weapon.getDamage() + " damage, "
                        + this.weapon.getAttackSpeed() + " attacks/sec, " + this.weapon.getRange() + " range, "
                        + this.weapon.getMaxMunitions() + " munitions.");
            } else if (this.weapon instanceof SniperRifle) {
                System.out.println("You already have a Sniper Rifle.");
            } else {
                System.out.println(
                        "Not enough gold : 200 gold required to buy a Sniper Rifle -> You only have " + this.gold);
            }
        }
        // --- DÉPLACEMENT ---
        moving = false;

        // Support diagonal movement by reading keys independently
        float base = speed * delta * 60f;
        float dx = 0f, dy = 0f;
        boolean pressRight = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean pressLeft = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean pressUp = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean pressDown = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (pressRight && !pressLeft)
            dx += base;
        if (pressLeft && !pressRight)
            dx -= base;
        if (pressUp && !pressDown)
            dy += base;
        if (pressDown && !pressUp)
            dy -= base;

        if (dx != 0f || dy != 0f) {
            tryMove(dx, dy, mapWidth, mapHeight, target);
            moving = true;
            // determine facing based on dx/dy signs
            if (dx > 0 && dy > 0) {
                direction = Direction.UP_RIGHT;
            } else if (dx < 0 && dy > 0) {
                direction = Direction.UP_LEFT;
            } else if (dx > 0 && dy < 0) {
                direction = Direction.DOWN_RIGHT;
            } else if (dx < 0 && dy < 0) {
                direction = Direction.DOWN_LEFT;
            } else if (dx > 0) {
                direction = Direction.RIGHT;
            } else if (dx < 0) {
                direction = Direction.LEFT;
            } else if (dy > 0) {
                direction = Direction.UP;
            } else if (dy < 0) {
                direction = Direction.DOWN;
            }
        }

        // --- ANIMATION ---
        if (moving) {
            stateTime += delta;
        } else {
            if (direction == Direction.ATTACKDOWN || direction == Direction.ATTACKLEFT
                    || direction == Direction.ATTACKRIGHT || direction == Direction.ATTACKUP) {

                stateTime += delta;

                // Si animation d'attaque finie → retour direction précédente
                float attackDur = getCurrentAttackAnimationDuration();
                if (attackDur > 0f && stateTime >= attackDur) {
                    direction = prevDirection;
                    stateTime = 0f;
                }

            } else {
                stateTime = 0f;
            }
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
                !checkHeroEnemyCollisions(newX, newY, closestEnemy)
                && !checkHeroSoldierCollisions(newX, newY, findClosestSoldier(this.allyBase.getUnits()))) {

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

        if (distance < this.getWidth()) {
            return true;
        }
        return false;
    }

    private boolean checkHeroSoldierCollisions(float newX, float newY, Unit soldier) {
        if (soldier == null || soldier.isDead()) {
            return false;
        }

        // Calculate distance between hero and soldier
        float dx = newX - soldier.getPosX();
        float dy = newY - soldier.getPosY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < this.getWidth()) {
            return true;
        }
        return false;
    }

    @Override
    /**
     * Find the closest enemy within attack range
     * 
     * @param units List of all enemy units
     * @param range Attack range
     * @return Closest enemy in range, or null if none found
     */
    protected Unit findClosestEnemy(List<Unit> enemies) {
        Unit closest = null;
        double minDistance = Double.MAX_VALUE;
        for (Unit enemy : enemies) {
            double distance = calculateDistance(enemy);
            if (distance < minDistance && !enemy.isDead()) {
                minDistance = distance;
                closest = enemy;
            }
        }
        return closest;
    }

    protected Unit findClosestSoldier(List<Unit> units) {
        Unit closest = null;
        double minDistance = Double.MAX_VALUE;
        for (Unit soldier : units) {
            double distance = calculateDistance(soldier);
            if (distance < minDistance && !soldier.isDead()) {
                minDistance = distance;
                closest = soldier;
            }
        }
        return closest;
    }

    /**
     * Find the closest enemy within attack range
     * 
     * @param units List of all enemy units
     * @return Closest enemy in range, or null if none found
     */
    protected Unit findClosestEnemyInRange(List<Unit> enemies) {
        Unit closest = null;
        double bestDist = Double.MAX_VALUE;

        for (Unit enemy : enemies) {
            if (enemy.isDead())
                continue;

            double distance = calculateDistance(enemy);
            if (distance < this.weapon.getRange() && distance < bestDist) {
                bestDist = distance;
                closest = enemy;
            }
        }
        return closest;
    }

    // Keep public methods for backward compatibility
    public void moveUp(float delta, float mapHeight, List<Unit> enemies) {
        direction = Direction.UP;
        tryMove(0, speed * delta * 60, Float.MAX_VALUE, mapHeight, findClosestEnemy(enemies));
    }

    public void moveDown(float delta, List<Unit> enemies) {
        direction = Direction.DOWN;
        tryMove(0, -speed * delta * 60, Float.MAX_VALUE, Float.MAX_VALUE, findClosestEnemy(enemies));
    }

    public void moveLeft(float delta, List<Unit> enemies) {
        direction = Direction.LEFT;
        tryMove(-speed * delta * 60, 0, Float.MAX_VALUE, Float.MAX_VALUE, findClosestEnemy(enemies));
    }

    public void moveRight(float delta, float mapWidth, List<Unit> enemies) {
        direction = Direction.RIGHT;
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
                // Save current non-attack facing so we can revert after animation
                if (direction == Direction.RIGHT || direction == Direction.LEFT || direction == Direction.UP
                        || direction == Direction.DOWN || direction == Direction.UP_RIGHT
                        || direction == Direction.UP_LEFT || direction == Direction.DOWN_RIGHT
                        || direction == Direction.DOWN_LEFT) {
                    prevDirection = direction;
                }
                if (direction == Direction.RIGHT) {
                    direction = Direction.ATTACKRIGHT;
                } else if (direction == Direction.LEFT) {
                    direction = Direction.ATTACKLEFT;
                } else if (direction == Direction.UP) {
                    direction = Direction.ATTACKUP;
                } else if (direction == Direction.DOWN) {
                    direction = Direction.ATTACKDOWN;
                }
                stateTime = 0f;

                weapon.attack();
                int totalDamage = weapon.getDamage();
                System.out.println(
                        "Hero attacks " + target.getClass().getSimpleName() + " for " + totalDamage + " damage");
                target.takeDamage(totalDamage);
                attackCooldown = weapon.getAttackSpeed();

                // Jouer le son de tir
                if (shootSound != null) {
                    System.out.println("🔊 SON DE TIR: Lecture du son...");
                    shootSound.play(0.7f); // Volume à 70%
                } else {
                    System.out.println("❌ ERREUR: shootSound est NULL!");
                }

            } else {
                weapon.reload();
            }
        }
    }

    @lombok.Generated
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame;
        float visualWidth;
        float visualHeight;

        switch (direction) {
            case RIGHT:
                if (moving) {
                    currentFrame = walkRight.getKeyFrame(stateTime, true);
                    visualWidth = 50;
                    visualHeight = 50;
                } else {
                    currentFrame = (idleR != null) ? idleR : idle;
                    visualWidth = 30;
                    visualHeight = 50;
                }
                break;
            case LEFT:
                if (moving) {
                    currentFrame = walkLeft.getKeyFrame(stateTime, true);
                    visualWidth = 50;
                    visualHeight = 50;
                } else {
                    currentFrame = (idleL != null) ? idleL : idle;
                    visualWidth = 30;
                    visualHeight = 50;
                }
                break;
            case UP:
                if (moving) {
                    currentFrame = walkUp.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = (idleU != null) ? idleU : idle;
                }
                visualWidth = 30;
                visualHeight = 50;
                break;
            case UP_RIGHT:
                if (moving) {
                    currentFrame = walkUR.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = (idleUR != null) ? idleUR : idle;
                }
                visualWidth = 40;
                visualHeight = 50;
                break;
            case UP_LEFT:
                if (moving) {
                    currentFrame = walkUL.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = (idleUL != null) ? idleUL : idle;
                }
                visualWidth = 40;
                visualHeight = 50;
                break;
            case DOWN_RIGHT:
                if (moving) {
                    currentFrame = walkDR.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = (idleDR != null) ? idleDR : idle;
                }
                visualWidth = 40;
                visualHeight = 50;
                break;
            case DOWN_LEFT:
                if (moving) {
                    currentFrame = walkDL.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = (idleDL != null) ? idleDL : idle;
                }
                visualWidth = 40;
                visualHeight = 50;
                break;
            case ATTACKDOWN:
                currentFrame = AttackDown.getKeyFrame(stateTime, false);
                visualWidth = 30;
                visualHeight = 50;
                break;
            case ATTACKRIGHT:
                currentFrame = AttackRight.getKeyFrame(stateTime, false);
                visualWidth = 40;
                visualHeight = 50;
                break;
            case ATTACKLEFT:
                currentFrame = AttackLeft.getKeyFrame(stateTime, false);
                visualWidth = 40;
                visualHeight = 50;
                break;
            case ATTACKUP:
                currentFrame = AttackUp.getKeyFrame(stateTime, false);
                visualWidth = 30;
                visualHeight = 50;
                break;
            default:
                if (moving) {
                    currentFrame = walkDown.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = (idleD != null) ? idleD : idle;
                }
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
     * @return true if there was enough gold to remove, false otherwise
     */
    public boolean removeGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    // === AUDIO SYSTEM ===

    /**
     * Set the shoot sound effect
     * 
     * @param shootSound Sound to play when hero shoots
     */
    public void setShootSound(Sound shootSound) {
        this.shootSound = shootSound;
    }
}