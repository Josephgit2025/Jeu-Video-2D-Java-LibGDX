package com.main;

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
                return new Tank();
                break;
            case "Cack":
                return new Soldier();
                break;
            case "Sniper":
                return new Sniper();
                break;
            default:
                return null;
                break;
        }
    }
}
