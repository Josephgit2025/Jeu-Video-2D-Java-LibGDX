package com.main.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.main.GameScreen;
import com.main.entities.Unit;
import com.main.entities.enemies.CZombie;
import com.main.entities.enemies.FZombie;
import com.main.entities.enemies.WZombie;
import com.main.entities.units.Melee;
import com.main.entities.units.Sniper;
import com.main.entities.units.Tank;
import com.main.utils.Position;

enum Type {
    MELEE,
    TANK,
    SNIPER,
    WOMAN,
    CRAWL,
    FAST,
}

public class Base {
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

    public Base(int posX, int posY, boolean isPlayerBase, int mapHeight) {
        this.position = new Position(posX, posY);
        lastSpawn = 0.0f;
        this.units = new ArrayList<>();
        random = new Random();
        this.isPlayerBase = isPlayerBase;
        this.name = isPlayerBase ? "PLAYER BASE" : "ENEMY BASE";
        
        // Créer la hitbox : 3 tuiles de large * TILE_SIZE * SCALE, hauteur totale de la map
        float boxWidth = 3 * TILE_SIZE * SCALE; // 3 tuiles * 16px * 2 = 96px
        float boxHeight = mapHeight; // Toute la hauteur de la map
        
        // Position : pour la base joueur (gauche), pour la base ennemie (droite - 3 tuiles)
        float boxX = isPlayerBase ? posX : (posX - boxWidth);
        float boxY = 0; // Du bas de la map
        
        this.collisionBox = new Rectangle(boxX, boxY, boxWidth, boxHeight);
        System.out.println(name + " collision box: x=" + boxX + " y=" + boxY + " width=" + boxWidth + " height=" + boxHeight);
    }

    public int getHealth() {
        return health;
    }

    public Position getPosition() {
        return position;
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
        System.out.println(">>> " + name + " takes " + damage + " damage! (HP: " + oldHealth + " -> " + this.health + ")");
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

    public Unit spawnUnit(GameScreen screen, float delta) {
        if (lastSpawn >= 5.0f) {
            lastSpawn = 0.0f;
            
            if (isPlayerBase) {
                // Spawn soldiers (left side)
                Type[] soldierTypes = {Type.TANK, Type.MELEE, Type.SNIPER};
                Type type = soldierTypes[random.nextInt(soldierTypes.length)];
                switch (type) {
                    case TANK:
                        System.out.println("Tank spawned");
                        return new Tank(0, random.nextInt(screen.getMapHeight()));
                    case MELEE:
                        System.out.println("Melee spawned");
                        return new Melee(0, random.nextInt(screen.getMapHeight()));
                    case SNIPER:
                        System.out.println("Sniper spawned");
                        return new Sniper(0, random.nextInt(screen.getMapHeight()));
                    default:
                        return null;
                }
            } else {
                // Spawn zombies (right side)
                Type[] zombieTypes = {Type.WOMAN, Type.CRAWL, Type.FAST};
                Type type = zombieTypes[random.nextInt(zombieTypes.length)];
                switch (type) {
                    case WOMAN:
                        System.out.println("Zombie women spawned");
                        return new WZombie(screen.getMapWidth(), random.nextInt(screen.getMapHeight()));
                    case CRAWL:
                        System.out.println("Zombie crawler spawned");
                        return new CZombie(screen.getMapWidth(), random.nextInt(screen.getMapHeight()));
                    case FAST:
                        System.out.println("Zombie fast spawned");
                        return new FZombie(screen.getMapWidth(), random.nextInt(screen.getMapHeight()));
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
     * @param delta Le temps écoulé
     * @param enemies La liste des ennemis à attaquer
     * @param enemyBase La base ennemie à attaquer si pas d'ennemis
     */
    public void updateUnits(float delta, List<Unit> enemies, Base enemyBase) {
        // Supprime les unités mortes
        units.removeIf(Unit::isDead);

        // Met à jour chaque unité
        for (Unit unit : units) {
            // Set enemy base as target
            unit.setTargetBase(enemyBase);
            
            unit.move(delta);
            
            // Filtre uniquement les ennemis vivants
            List<Unit> liveEnemies = new ArrayList<>();
            if (enemies != null) {
                for (Unit enemy : enemies) {
                    if (!enemy.isDead()) {
                        liveEnemies.add(enemy);
                    }
                }
            }
            
            unit.selectTarget(liveEnemies);
            unit.updateCooldown(delta);
            unit.attack();
        }
    }
}