package com.main.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Unit {
    protected float posX;
    protected float posY;
    protected Sprite sprite;
    protected int health;
    protected int attackDamage;
    protected int attackSpeed;
    protected float speed;
    protected Unit target;
    // protected List<Effect> modifiers = new ArrayList<>(); // TODO: Créer la
    // classe Effect si nécessaire
    protected int range;
    protected int attackCooldown = 0;
    protected Texture texture;
    protected float width, height;
    

    
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
        this.width = 32;  // Hitbox correspond à la largeur
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

    public int getHealth(){
        return this.health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public Unit getTarget() {
        return target;
    }

    public int getRange() {
        return range;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setCooldown(int cd){
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
            this.target = findClosestEnemy(inRange);
        } else {
            this.target = null;
        }
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
    public void updateCooldown() {
        if (attackCooldown > 0) {
            attackCooldown--;
        }
    }

    /**
     * Appelé quand l'unité meurt
     */
    protected void onDeath() {
        // Logique de mort (animation, suppression, etc.)
        // À personnaliser dans les sous-classes si nécessaire
    }

    /**
     * Vérifie si l'unité est morte
     */
    public boolean isDead() {
        return this.health <= 0;
    }

    public void attack() {
        if (target == null || target.isDead()) {
            return; // Pas de cible valide
        }
        if (attackCooldown <= 0) {
            target.takeDamage(attackDamage);
            attackCooldown = attackSpeed;
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
        // Default: move to the right. Subclasses should override.
        this.setSpritePosX(this.posX + this.speed * delta);
    }
}