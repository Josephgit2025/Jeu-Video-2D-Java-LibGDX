package com.main.map;

import com.main.utils.Position;

import java.util.Random;

import com.main.entities.Unit;
import com.main.entities.units.Tank;
import com.main.entities.units.Melee;
import com.main.entities.units.Sniper;

enum Type{
    MELEE,
    TANK,
    SNIPER
}

public class Base {
    private int health = 1000;
    private Position position;
    private int attackPower = 50;
    private float lastSpawn;

    public Base(int posX, int posY){
        this.position = new Position(posX, posY);
        lastSpawn = 0.0f;
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

    public void takeDamage(int damage){
        this.health -= damage;
    }

    public Unit spawnUnit(float delta){
        if (lastSpawn >= 5.0f){
            Type type = Type.values()[new Random().nextInt(Type.values().length)];
            switch (type){
                case TANK:
                    lastSpawn = 0.0f;
                    return new Tank(400,200);
                case MELEE:
                    lastSpawn = 0.0f;
                    return new Melee(400, 200);
                case SNIPER:
                    lastSpawn = 0.0f;
                    return new Sniper(400,200);
                default:
                    return null; 
            }
        }
        lastSpawn += delta;
        return null;
    }   
}