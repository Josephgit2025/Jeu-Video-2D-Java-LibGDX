package com.main.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.main.map.Base;

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
    protected Base targetBase; // Reference to enemy base
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
    protected static final int BASE_ATTACK_RANGE = 100; // Range to attack base

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
     * Vérifie si un mouvement vers newX causerait une collision avec la hitbox de la base ennemie
     */
    protected boolean wouldCollideWithBase(float newX) {
        if (targetBase == null || targetBase.getCollisionBox() == null) {
            return false;
        }
        
        // Créer un rectangle temporaire pour la nouvelle position
        Rectangle unitRect = new Rectangle(newX, this.posY, this.width, this.height);
        
        // Vérifier la collision avec la hitbox de la base
        boolean collides = unitRect.overlaps(targetBase.getCollisionBox());
        
        if (collides) {
            System.out.println(this.getClass().getSimpleName() + " BLOCKED by " + targetBase.getName() + " hitbox!");
        }
        
        return collides;
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
     * Si aucune unité ennemie n'est disponible, cible la base ennemie
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
            // No enemy units available, target enemy base if set
            this.target = null;
        }
    }
    
    public void setTargetBase(Base enemyBase) {
        this.targetBase = enemyBase;
    }
    
    public Base getTargetBase() {
        return targetBase;
    }
    
    public void setTarget(Unit target){
        if (target != null){
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
        // Priority 1: Attack unit target if available
        if (target != null && !target.isDead()) {
            double distance = calculateDistance(target);
            if (distance <= this.range && attackCooldown <= 0) {
                System.out.println(this.getClass().getSimpleName() + " attacks " + target.getClass().getSimpleName() + 
                                 " (HP: " + target.getHealth() + " -> " + (target.getHealth() - attackDamage) + ")");
                target.takeDamage(attackDamage);
                attackCooldown = attackSpeed;
                currentState = UnitState.ATTACKING;
                attackAnimationTimer = ATTACK_ANIMATION_DURATION;
                return;
            }
        }
        
        // Priority 2: Attack enemy base if in range and no units to fight
        if (targetBase != null && target == null) {
            double distanceToBase = calculateDistanceToBase();
            if (distanceToBase <= BASE_ATTACK_RANGE && attackCooldown <= 0) {
                System.out.println(this.getClass().getSimpleName() + " attacks enemy BASE" + 
                                 " (HP: " + targetBase.getHealth() + " -> " + (targetBase.getHealth() - attackDamage) + ")");
                targetBase.takeDamage(attackDamage);
                attackCooldown = attackSpeed;
                currentState = UnitState.ATTACKING;
                attackAnimationTimer = ATTACK_ANIMATION_DURATION;
                return;
            }
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
    
    /**
     * Calcule la distance entre cette unité et la base ennemie
     */
    protected double calculateDistanceToBase() {
        if (targetBase == null) return Double.MAX_VALUE;
        
        float baseX = targetBase.getPosition().getPosX();
        float baseY = targetBase.getPosition().getPosY();
        
        double deltaX = baseX - this.posX;
        double deltaY = baseY - this.posY;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    
    /**
     * Vérifie si l'unité doit s'arrêter (cible à portée ou base à portée)
     * @return true si l'unité doit s'arrêter
     */
    protected boolean shouldStopMoving() {
        // Stop if attacking
        if (attackAnimationTimer > 0) {
            return true;
        }
        
        // Stop if unit target in range
        if (target != null && !target.isDead()) {
            double distance = calculateDistance(target);
            if (distance <= this.range) {
                return true;
            }
        }
        
        // Stop if base target in range
        if (target == null && targetBase != null) {
            double distanceToBase = calculateDistanceToBase();
            if (distanceToBase <= BASE_ATTACK_RANGE) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Calcule la nouvelle position X après mouvement, en tenant compte de la direction
     * @param delta temps écoulé
     * @param direction direction du mouvement (1 = droite, -1 = gauche)
     * @return nouvelle position X, ou position actuelle si collision
     */
    protected float calculateNewPositionX(float delta, int direction) {
        float newX = this.posX + (this.speed * delta * direction);
        
        // Check collision with enemy base hitbox
        if (wouldCollideWithBase(newX)) {
            return this.posX; // Stay in place
        }
        
        return newX;
    }

    public void specialAbility() {
        // Implement special ability logic here
    }

    /**
     * Move the unit for this frame. Default implementation moves right.
     * Subclasses should override to provide specific movement behavior.
     * @param delta Le temps écoulé depuis la dernière frame
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