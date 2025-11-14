package com.main.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.main.GameScreen;
import com.main.entities.Unit;
import com.main.entities.enemies.CZombie;
import com.main.entities.enemies.FZombie;
import com.main.entities.enemies.WZombie;
import com.main.entities.player.Hero;
import com.main.entities.units.Melee;
import com.main.entities.units.Sniper;
import com.main.entities.units.Tank;
import com.main.utils.Position;

public class Base {
    
    public enum Type {
        MELEE,
        TANK,
        SNIPER,
        WOMAN,
        CRAWL,
        FAST
    }
    
    private int health = 1000;
    private Position position;
    private int attackPower = 50;
    private float lastSpawn;
    private List<Unit> units; // Unités de cette base
    private Random random;
    private boolean isPlayerBase; // true = spawn soldiers, false = spawn zombies
    private String name; // Name for debugging
    private Rectangle collisionBox; // Hitbox de la base (3 tuiles de large x hauteur de la map)
    private static final int TILE_SIZE = 16; // Taille d'une tuile dans Tiled
    private static final float SCALE = 2.0f; // Scale de la map
    private int[] spawnPointsY;

    public Base(int posX, int posY, boolean isPlayerBase, int mapHeight) {
        this.position = new Position(posX, posY);
        lastSpawn = 0.0f;
        this.units = new ArrayList<>();
        random = new Random();
        this.isPlayerBase = isPlayerBase;
        this.name = isPlayerBase ? "PLAYER BASE" : "ENEMY BASE";
        this.spawnPointsY = new int[3];
        this.spawnPointsY[0] = mapHeight / 4; // Haut
        this.spawnPointsY[1] = mapHeight / 2; // Milieu
        this.spawnPointsY[2] = (3 * mapHeight) / 4; // Bas

        // Créer la hitbox : 3 tuiles de large * TILE_SIZE * SCALE, hauteur totale de la
        // map
        float boxWidth = 3 * TILE_SIZE * SCALE; // 3 tuiles * 16px * 2 = 96px
        float boxHeight = mapHeight; // Toute la hauteur de la map

        // Position : pour la base joueur (gauche), pour la base ennemie (droite - 3
        // tuiles)
        float boxX = isPlayerBase ? posX : (posX - boxWidth);
        float boxY = 0; // Du bas de la map

        this.collisionBox = new Rectangle(boxX, boxY, boxWidth, boxHeight);
        // System.out.println(
        //         name + " collision box: x=" + boxX + " y=" + boxY + " width=" + boxWidth + " height=" + boxHeight);
    }

    public int getHealth() {
        return health;
    }

    public Position getPosition() {
        return position;
    }

    public float getPosX() {
        return position.getPosX();
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void takeDamage(int damage) {
        int oldHealth = this.health;
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
        System.out.println(
                ">>> " + name + " takes " + damage + " damage! (HP: " + oldHealth + " -> " + this.health + ")");
    }

    public boolean isDestroyed() {
        return this.health <= 0;
    }

    public String getName() {
        return name;
    }

    public Rectangle getCollisionBox() {
        return collisionBox;
    }

    public void addUnit(Unit unit) {
        if (unit != null) {
            this.units.add(unit);
        }
    }

    public List<Unit> getUnits() {
        return this.units;
    }

    public Unit buyUnit(Type unitType, int spawnIndex, Hero hero) {
        // spawnIndex = 0, 1 ou 2 (pour les 3 points Y)
        // Vérifie le coût selon le type
        // Vérifie si hero a assez d'or
        // Retire l'or et crée l'unité
        int spawnY = spawnPointsY[spawnIndex];
        switch (unitType) {
            case MELEE:
                if (hero.getGold() >= Melee.COST) {
                    hero.removeGold(Melee.COST);
                    System.out.println(
                            "Melee unit bought for " + Melee.COST + " gold. Hero gold left: " + hero.getGold());
                    return new Melee(100, spawnY);
                } else {
                    System.out.println("Not enough gold to buy Melee unit.");
                }
                break;
            case TANK:
                if (hero.getGold() >= Tank.COST) {
                    hero.removeGold(Tank.COST);
                    System.out
                            .println("Tank unit bought for " + Tank.COST + " gold. Hero gold left: " + hero.getGold());
                    return new Tank(100, spawnY);
                } else {
                    System.out.println("Not enough gold to buy Tank unit.");
                }
                break;
            case SNIPER:
                if (hero.getGold() >= Sniper.COST) {
                    hero.removeGold(Sniper.COST);
                    System.out
                            .println("Sniper unit bought for " + Sniper.COST + " gold. Hero gold left: "
                                    + hero.getGold());
                    return new Sniper(100, spawnY);
                } else {
                    System.out.println("Not enough gold to buy Sniper unit.");
                }
                break;
            default:
                System.out.println("Unknown unit type.");
                break;
        }
        return null;
    }

    public Unit spawnUnit(GameScreen screen, float delta) {
        if (lastSpawn >= 5.0f) {
            lastSpawn = 0.0f;

            if (isPlayerBase) {
                // Spawn soldiers (left side)
                Type[] soldierTypes = { Type.TANK, Type.MELEE, Type.SNIPER };
                Type type = soldierTypes[random.nextInt(soldierTypes.length)];
                switch (type) {
                    case TANK:
                        System.out.println("Tank spawned");
                        return new Tank(100, spawnPointsY[random.nextInt(3)]);
                    case MELEE:
                        System.out.println("Melee spawned");
                        return new Melee(100, spawnPointsY[random.nextInt(3)]);
                    case SNIPER:
                        System.out.println("Sniper spawned");
                        return new Sniper(100, spawnPointsY[random.nextInt(3)]);
                    default:
                        return null;
                }
            } else {
                // Spawn zombies (right side)
                Type[] zombieTypes = { Type.WOMAN, Type.CRAWL, Type.FAST };
                Type type = zombieTypes[random.nextInt(zombieTypes.length)];
                switch (type) {
                    case WOMAN:
                        System.out.println("Zombie women spawned");
                        return new WZombie(screen.getMapWidth(), spawnPointsY[random.nextInt(3)]);
                    case CRAWL:
                        System.out.println("Zombie crawler spawned");
                        return new CZombie(screen.getMapWidth(), spawnPointsY[random.nextInt(3)]);
                    case FAST:
                        System.out.println("Zombie fast spawned");
                        return new FZombie(screen.getMapWidth(), spawnPointsY[random.nextInt(3)]);
                    default:
                        return null;
                }
            }
        }
        lastSpawn += delta;
        return null;
    }

    /**
     * Met à jour toutes les unités de cette base
     * 
     * @param delta     Le temps écoulé
     * @param enemies   La liste des ennemis à attaquer
     * @param enemyBase La base ennemie à attaquer si pas d'ennemis
     */
    public void updateUnits(float delta, List<Unit> enemies, Base enemyBase, Hero hero) {
        // Supprime les unités mortes
        units.removeIf(Unit::isDead);

        // Met à jour chaque unité
        for (Unit unit : units) {
            // Set enemy base as target
            unit.setTargetBase(enemyBase);

            // Filtre uniquement les ennemis vivants
            List<Unit> liveEnemies = new ArrayList<>();
            if (enemies != null) {
                for (Unit enemy : enemies) {
                    if (!enemy.isDead()) {
                        liveEnemies.add(enemy);
                    }
                }
            }
            if (hero != null)
                liveEnemies.add(hero);

            // Determine target and update cooldown BEFORE moving so move(delta) sees the
            // correct state
            unit.selectTarget(liveEnemies);
            unit.updateCooldown(delta);

            // À la fin de la boucle, après la gestion des attaques entre unités
            if (unit.target == null && unit.isNearEnemyBase(enemyBase)) {
                unit.attackBase(enemyBase);
            }

            // Move will handle attack triggering and animation timing internally
            unit.move(delta);
        }
    }
}