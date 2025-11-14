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
    public Unit target;
    protected Base targetBase; // Reference to enemy base
    protected Base allyBase;
    protected int range;
    protected float attackCooldown = 0f; // Current cooldown in seconds
    protected Texture texture;
    protected float width, height;
    private int lane;
    private int index;

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

    public int getLane(){
        return lane;
    }

    public float getSpeed() {
        return speed;
    }

    public float getWidth() {
        return width;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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


    public float getAttackAnimationTimer() {
        return attackAnimationTimer;
    }

    public void setCooldown(float cd) {
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

    public void setLane(int lane){
        this.lane = lane;
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
     * Vérifie si un mouvement vers newX causerait une collision avec la hitbox de
     * la base ennemie
     */
    protected boolean wouldCollideWithBase(float newX) {
        if (targetBase == null || targetBase.getCollisionBox() == null) {
            return false;
        }

        // Créer un rectangle temporaire pour la nouvelle position
        Rectangle unitRect = new Rectangle(newX, this.posY, this.width, this.height);

        // Vérifier la collision avec la hitbox de la base
        boolean collides = unitRect.overlaps(targetBase.getCollisionBox());

        // if (collides) {
        // System.out.println(this.getClass().getSimpleName() + " BLOCKED by " +
        // targetBase.getName() + " hitbox!");
        // }

        return collides;
    }

    /**
     * Calcule la distance entre cette unité et une autre
     */
    private double calculateDistance(Unit other) {
        float dx = this.posX - other.getPosX();
        float dy = this.posY - other.getPosY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Détecte tous les ennemis à portée d'attaque
     */
    public List<Unit> detectEnemiesInRange(List<Unit> enemies) {
        List<Unit> inRange = new ArrayList<>();
        for (Unit unit : enemies) {
            if (unit != this && !unit.isDead()) {
                double distance = calculateDistance(unit);
                if ((int)distance <= this.range) {
                    inRange.add(unit);
                }
            }
        }
        return inRange;
    }

    /**
     * Trouve l'ennemi le plus proche dans une liste
     */
    protected Unit findClosestEnemy(List<Unit> enemies) {
        Unit closest = null;
        double minDistance = Double.MAX_VALUE;
        for (Unit enemy : enemies) {
            double distance = calculateDistance(enemy);
            if (distance < minDistance && !enemy.isDead() && enemy.getLane() == this.lane) {
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
        if (this.health <= 0){
            if (this.allyBase.getUnitsPerLane().get(this.lane).contains(this)){
                for (Unit elem : this.allyBase.getUnitsPerLane().get(this.lane)){
                    elem.index--;
                }
                this.allyBase.getUnitsPerLane().get(this.lane).remove(this);
            }
            return true;
        }
        return false;
    }

    /**
     * Attaque la cible actuelle (unité)
     */
    public void attack() {
        if (target != null && !target.isDead() && attackCooldown <= 0) {
            double distance = calculateDistance(target);
            if (distance <= this.range) {
                System.out.println(this.getClass().getSimpleName() + " attacks " + target.getClass().getSimpleName() +
                        " (HP: " + target.getHealth() + " -> " + (target.getHealth() - attackDamage) + ")");
                target.takeDamage(attackDamage);
                attackCooldown = attackSpeed;
                currentState = UnitState.ATTACKING;
                attackAnimationTimer = getAttackAnimationDuration();
                this.stateTime = 0f;
                return;
            }
        }

        // Priority 2: Attack enemy base if in range and no units to fight
        // if (targetBase != null && target == null) {
        //     double distanceToBase = calculateDistanceToBase();
        //     if (distanceToBase <= BASE_ATTACK_RANGE && attackCooldown <= 0) {
        //         System.out.println(this.getClass().getSimpleName() + " attacks enemy BASE" +
        //                 " (HP: " + targetBase.getHealth() + " -> " + (targetBase.getHealth() - attackDamage) + ")");
        //         targetBase.takeDamage(attackDamage);
        //         attackCooldown = attackSpeed;
        //         currentState = UnitState.ATTACKING;
        //         attackAnimationTimer = getAttackAnimationDuration();
        //         this.stateTime = 0f;
        //         return;
        //     }
        // }
        // No default fallback here; attacks are handled above for targets or base.
    }


    // Méthode pour attaquer la base
    public void attackBase(Base enemyBase) {
        if (enemyBase != null && attackCooldown <= 0) {
            System.out.println(">>> " + this.getClass().getSimpleName() + " at (" + (int) posX + "," + (int) posY +
                    ") ATTACKS " + enemyBase.getName() +
                    " (BASE HP: " + enemyBase.getHealth() + " -> " + (enemyBase.getHealth() - attackDamage) + ")");
            enemyBase.takeDamage(this.attackDamage);
            attackCooldown = attackSpeed;
            // Use subclass-specific attack animation duration when available
            attackAnimationTimer = getAttackAnimationDuration();
            currentState = UnitState.ATTACKING;
            this.stateTime = 0f;
            System.out.println("[ANIM] " + this.getClass().getSimpleName() + " base attack timer set: " + attackAnimationTimer);
        }
    }

    // Méthode pour vérifier si proche de la base
    public boolean isNearEnemyBase(Base base) {
        if (base == null)
            return false;
        Rectangle baseBox = base.getCollisionBox();
        if (baseBox == null)
            return false;

        // Calculer la distance entre l'unité et la hitbox de la base
        float unitCenterX = this.posX + (this.width / 2);
        float baseCenterX = baseBox.x + (baseBox.width / 2);
        float distance = Math.abs(unitCenterX - baseCenterX);

        boolean isNear = distance <= BASE_ATTACK_RANGE;
        if (isNear && target == null) {
            // System.out.println("🎯 " + this.getClass().getSimpleName() + " near " +
            // base.getName() +
            // " (distance: " + (int)distance + "/" + BASE_ATTACK_RANGE + ")");
        }

        return isNear;
    }

    /**
     * Vérifie si l'unité doit s'arrêter (cible à portée ou base à portée)
     * 
     * @return true si l'unité doit s'arrêter
     */
    protected boolean shouldStopMoving() {
        // Stop if attacking
        if (attackAnimationTimer > 0) {
            return true;
        }
        
        // Stop if unit in front is attacking and target not in range
        if (this.index != 0 && target != null && !target.isDead() && calculateDistance(target) >= this.range && this.allyBase.getUnitsPerLane().get(this.lane).get(this.index - 1).currentState == UnitState.ATTACKING){
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
        if (target == null && isNearEnemyBase(targetBase)) {
            return true;
        }


        return false;
    }

    private boolean checkUnitCollisions(float newX, float newY) {

        // Calculate distance between hero and ally
        float distance = 0;
        if (this.index == 0){
            return false;
        }
        if (this.index != 0){
            float dx = newX - this.allyBase.getUnitsPerLane().get(this.getLane()).get(this.index - 1).getPosX();
            float dy = newY - this.allyBase.getUnitsPerLane().get(this.getLane()).get(this.index - 1).getPosY();
            distance = (float) Math.sqrt(dx * dx + dy * dy);
        }

        if (distance < this.getWidth()) {
            return true;
        }
        return false;
    }

    /**
     * Calcule la nouvelle position X après mouvement, en tenant compte de la
     * direction
     * 
     * @param delta     temps écoulé
     * @param direction direction du mouvement (1 = droite, -1 = gauche)
     * @return nouvelle position X, ou position actuelle si collision
     */
    protected float calculateNewPositionX(float delta, int direction) {
        float newX = this.posX + (this.speed * delta * direction);

        // Check collision with enemy base hitbox
        if (wouldCollideWithBase(newX) || checkUnitCollisions(newX, posY)) {
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
     * 
     * @param delta Le temps écoulé depuis la dernière frame
     */
    public void move(float delta) {
        if (currentState == UnitState.ATTACKING) {
            float before = attackAnimationTimer;
            attackAnimationTimer -= delta;
            // advance shared animation timer so attack animations progress when
            // using default move implementation
            this.stateTime += delta;
            if (before > 0 && attackAnimationTimer <= 0) {
                System.out.println("[ANIM] " + this.getClass().getSimpleName() + " attack animation finished (was " + before + ")");
            }
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
        // If no unit target, handle enemy base: attack when in range, otherwise move toward it
        if (targetBase != null) {
            float baseX = targetBase.getPosition().getPosX();
            float baseY = targetBase.getPosition().getPosY();
            double distanceToBase = Math.sqrt(Math.pow(baseX - this.posX, 2) + Math.pow(baseY - this.posY, 2));
            if (distanceToBase <= BASE_ATTACK_RANGE) {
                // In range of base: attempt an attack if cooldown ready
                if (attackCooldown <= 0) {
                    // Use dedicated base attack helper so we apply base damage and animation
                    attackBase(targetBase);
                    this.stateTime = 0f;
                    return;
                } else {
                    currentState = UnitState.IDLE;
                    this.stateTime += delta;
                    return;
                }
            } else {
                // Not yet in base range: walk towards the base
                currentState = UnitState.WALKING;
                int direction = (targetBase.getPosition().getPosX() > posX) ? 1 : -1;
                setSpritePosX(calculateNewPositionX(delta, direction));
                this.stateTime += delta;
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