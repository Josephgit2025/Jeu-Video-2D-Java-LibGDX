package com.main.entities;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Unit {
    protected int posX;
    protected int posY;
    protected Image spriteSheet;
    protected ImageView sprite;
    protected int health;
    protected int attackDamage;
    protected int attackSpeed;
    protected int speed;
    protected Unit target;
    protected List<Effect> modifiers = new ArrayList<>();
    protected int range;
    protected int attackCooldown;

    public Unit(String filePath, int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.spriteSheet = new Image(getClass().getResourceAsStream(filePath));
        this.sprite = new ImageView(spriteSheet);
        this.sprite.setFitHeight(120);
        this.sprite.setFitWidth(160);
        this.sprite.setTranslateX(posX);
        this.sprite.setTranslateY(posY);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public ImageView getSprite() {
        return sprite;
    }

    public void setSpritePosX(int posX) {
        this.sprite.setTranslateX(posX);
        this.posX = posX;
    }

    public void setSpritePosY(int posY) {
        this.sprite.setTranslateY(posY);
        this.posY = posY;
    }

    /**
     * Calcule la distance entre cette unité et une autre
     */
    private double calculateDistance(Unit other) {
        int dx = this.posX - other.posX;
        int dy = this.posY - other.posY;
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
}
