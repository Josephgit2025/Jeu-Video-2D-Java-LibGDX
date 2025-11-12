package com.main.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Unit {

    // Unit states for animation control
    public enum UnitState {
        IDLE, // Standing still
        WALKING, // Moving towards target
        ATTACKING // Playing attack animation
    }

    protected float posX;
    protected float posY;
    protected Sprite sprite;
    protected int health;
    protected int attackDamage;
    protected float attackSpeed; // Time in seconds between attacks
    protected float speed;
    protected Unit target;
    // protected List<Effect> modifiers = new ArrayList<>(); // TODO: Créer la
    // classe Effect si nécessaire
    protected int range;
    protected float attackCooldown = 0f; // Current cooldown in seconds
    protected Texture texture;
    protected float width, height;

    // State management
    protected UnitState currentState = UnitState.WALKING;
    protected float attackAnimationTimer = 0f; // Tracks attack animation progress
    protected static final float ATTACK_ANIMATION_DURATION = 0.5f; // Duration of attack animation
    // Shared animation time counter for subclasses to use when rendering
    protected float stateTime = 0f;

    /**
     * Hook for subclasses to override the duration of their attack animation.
     * Default implementation returns the global ATTACK_ANIMATION_DURATION.
     */
    protected float getAttackAnimationDuration() {
        return ATTACK_ANIMATION_DURATION;
    }

    public Unit(String filePath, float posX, float posY) {
        this.posX = posX;
        this.posY = posY;

        // Gérer le cas null pour les tests
        if (filePath != null) {
            this.texture = new Texture(filePath);
            this.sprite = new Sprite(texture);
        } else {
            this.texture = null;
            this.sprite = null;
        }

        if (this.sprite != null) {
            this.sprite.setPosition(posX, posY);
            this.sprite.setSize(32, 48); // Taille visuelle
        }
        this.width = 32; // Hitbox correspond à la largeur
        this.height = 48; // Hitbox complète
    }

    public float getPosX() {
        return posX;
    }

    public float getSpeed() {
        return speed;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getPosY() {
        return posY;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getHealth() {
        return this.health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public Unit getTarget() {
        return target;
    }

    public int getRange() {
        return range;
    }

    public float getAttackCooldown() {
        return attackCooldown;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setCooldown(int cd) {
        this.attackCooldown = cd;
    }

    public void setSpritePosX(float posX) {
        this.sprite.setX(posX);
        this.posX = posX;
    }

    public void setSpritePosY(float posY) {
        this.sprite.setY(posY);
        this.posY = posY;
    }

    /**
     * Dessine le sprite de l'unité
     */
    public void render(SpriteBatch batch) {
        batch.draw(this.texture, posX, posY);
    }

    /**
     * Libère les ressources (texture)
     */
    public void dispose() {
        texture.dispose();
    }

    /**
     * Calcule la distance entre cette unité et une autre
     */
    private double calculateDistance(Unit other) {
        float dx = this.posX - other.posX;
        float dy = this.posY - other.posY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Détecte tous les ennemis à portée d'attaque
     */
    public List<Unit> detectEnemiesInRange(List<Unit> enemies) {
        List<Unit> inRange = new ArrayList<>();
        for (Unit unit : enemies) {
            if (unit != this) {
                double distance = calculateDistance(unit);
                if (distance <= this.range) {
                    inRange.add(unit);
                }
            }
        }
        return inRange;
    }

    /**
     * Trouve l'ennemi le plus proche dans une liste
     */
    private Unit findClosestEnemy(List<Unit> enemies) {
        Unit closest = null;
        double minDistance = Double.MAX_VALUE;
        for (Unit enemy : enemies) {
            double distance = calculateDistance(enemy);
            if (distance < minDistance) {
                minDistance = distance;
                closest = enemy;
            }
        }
        return closest;
    }

    /**
     * Sélectionne automatiquement la cible la plus proche
     */
    public void selectTarget(List<Unit> enemies) {
        List<Unit> inRange = detectEnemiesInRange(enemies);
        if (!inRange.isEmpty()) {
            Unit newTarget = findClosestEnemy(inRange);
            if (this.target != newTarget && newTarget != null) {
                System.out.println(this.getClass().getSimpleName() + " at (" + (int) posX + "," + (int) posY +
                        ") targets " + newTarget.getClass().getSimpleName() +
                        " at (" + (int) newTarget.getPosX() + "," + (int) newTarget.getPosY() +
                        ") - distance: " + (int) calculateDistance(newTarget) + "/" + range);
            }
            this.target = newTarget;
        } else {
            this.target = null;
        }
    }

    public void setTarget(Unit target) {
        if (target != null) {
            this.target = target;
        }
    }

    /**
     * Inflige des dégâts à cette unité
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            onDeath();
        }
    }

    /**
     * Met à jour le cooldown d'attaque
     */
    public void updateCooldown(float delta) {
        if (attackCooldown > 0) {
            attackCooldown -= delta; // Decrement by time in seconds
            if (attackCooldown < 0) {
                attackCooldown = 0;
            }
        }
    }

    /**
     * Appelé quand l'unité meurt
     */
    protected void onDeath() {
        // Animation ou effet visuel de mort à personnaliser ici
        // Exemple : déclencher une animation, jouer un son, etc.
    }

    /**
     * Vérifie si l'unité est morte
     */
    public boolean isDead() {
        return this.health <= 0;
    }

    public void attack() {
        if (target == null || target.isDead()) {
            currentState = UnitState.WALKING;
            return; // Pas de cible valide
        }
        // Vérifie la portée
        double distance = calculateDistance(target);
        if (distance > this.range) {
            currentState = UnitState.WALKING;
            return; // Cible hors de portée
        }
        if (attackCooldown <= 0) {
            System.out.println(this.getClass().getSimpleName() + " attacks " + target.getClass().getSimpleName() +
                    " (HP: " + target.getHealth() + " -> " + (target.getHealth() - attackDamage) + ")");
            target.takeDamage(attackDamage);
            attackCooldown = attackSpeed;
            // Trigger attack animation and reset stateTime (subclasses may override
            // getAttackAnimationDuration() to provide a specific duration)
            currentState = UnitState.ATTACKING;
            attackAnimationTimer = getAttackAnimationDuration();
            this.stateTime = 0f;
        }
    }

    public void specialAbility() {
        // Implement special ability logic here
    }

    /**
     * Move the unit for this frame. Implementations should use delta time
     * (seconds) and the unit's speed (pixels/second) to update position.
     */
    public void move(float delta) {
        if (currentState == UnitState.ATTACKING) {
            attackAnimationTimer -= delta;
            // advance shared animation timer so attack animations progress when
            // using default move implementation
            this.stateTime += delta;
            if (attackAnimationTimer > 0) {
                return;
            }
            if (target != null && !target.isDead()) {
                if (attackCooldown <= 0) {
                    attack();
                } else {
                    currentState = UnitState.IDLE;
                }
            } else {
                // Cible morte → repasser à WALKING
                currentState = UnitState.WALKING;
                target = null;
            }
            return;
        }

        if (target != null) {
            if (target.isDead()) {
                target = null;
                currentState = UnitState.WALKING;
                return;
            }

            double distance = calculateDistance(target);

            if (distance <= this.range) {
                attack();
                return;
            } else {
                currentState = UnitState.WALKING;
                float direction = (target.getPosX() > posX) ? 1 : -1;
                setSpritePosX(posX + direction * speed * delta);
                return;
            }
        }
        currentState = UnitState.IDLE;
        // advance idle animation timer for units using default move()
        this.stateTime += delta;
    }

    /**
     * Get the current state of the unit (for animation purposes)
     */
    public UnitState getCurrentState() {
        return currentState;
    }
}