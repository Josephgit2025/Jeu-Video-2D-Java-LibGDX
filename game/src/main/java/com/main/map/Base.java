package com.main.map;

import com.main.utils.Position;
import com.main.entities.Unit;
import com.main.entities.units.Tank;
import com.main.entities.units.Melee;
import com.main.entities.units.Sniper;

public class Base {
    private int health = 1000;
    private Position position;
    private int attackPower = 50;

    public Base(int posX, int posY){
        this.position = new Position(posX, posY);
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

    private Unit spawnUnit(String type){
        switch (type){
            case "Tank":
                return new Tank(0,0);
<<<<<<< HEAD:game/src/main/java/com/main/Base.java
            case "Cack":
=======
            case "Melee":
>>>>>>> master:game/src/main/java/com/main/map/Base.java
                return new Melee(0,0);
            case "Sniper":
                return new Sniper(0,0);
            default:
<<<<<<< HEAD:game/src/main/java/com/main/Base.java
                return null;
=======
                return null; 
>>>>>>> master:game/src/main/java/com/main/map/Base.java
        }
    }   
}
