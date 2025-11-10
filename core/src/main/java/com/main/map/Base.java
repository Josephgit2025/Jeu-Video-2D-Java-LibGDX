package com.main.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private List<Unit> units;
    private Random random;

    public Base(int posX, int posY) {
        this.position = new Position(posX, posY);
        lastSpawn = 0.0f;
        this.units = new ArrayList<>();
        random = new Random();
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
        this.health -= damage;
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
            Type type = Type.values()[random.nextInt(Type.values().length)];
            switch (type) {
                case TANK:
                    lastSpawn = 0.0f;
                    System.out.println("Tank spawned");
                    return new Tank(0, random.nextInt(screen.getMapHeight()));
                case MELEE:
                    lastSpawn = 0.0f;
                    System.out.println("melee spawned");
                    return new Melee("zombie/women/Walk2.png", 0, random.nextInt(screen.getMapHeight()));
                case SNIPER:
                    lastSpawn = 0.0f;
                    System.out.println("sniper spawned");
                    return new Sniper("zombie/women/Walk3.png", 0, random.nextInt(screen.getMapHeight()));
                case WOMAN:
                    lastSpawn = 0.0f;
                    System.out.println("Zombie women");
                    return new WZombie(screen.getMapWidth(), random.nextInt(screen.getMapHeight()));
                case CRAWL:
                    lastSpawn = 0.0f;
                    System.out.println("Zombie crawler");
                    return new CZombie(screen.getMapWidth(), random.nextInt(screen.getMapHeight()));
                case FAST:
                    lastSpawn = 0.0f;
                    System.out.println("Zombie fast");
                    return new FZombie(screen.getMapWidth(), random.nextInt(screen.getMapHeight()));
                default:
                    return null;
            }
        }
        lastSpawn += delta;
        return null;
    }

    public void updateUnits(float delta) {
        for (Unit elem : units) {
            elem.move(delta);
        }
    }
}