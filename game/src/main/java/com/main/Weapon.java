package com.main;

public abstract class Weapon {
    protected int damage;
    protected int range;
    protected int attackSpeed;
    protected int munitions;
    protected int maxMunition;
    
    protected Weapon(int damage, int range, int as, int munitions, int maxMun){
        this.damage = damage;
        this.range = range;
        this.attackSpeed = as;
        this.munitions = munitions;
        this.maxMunition = maxMun;
    }

    protected void reload(){
        this.munitions = this.maxMunition;
    }

    public int getDamage() {
        return damage;
    }
    public int getRange() {
        return range;
    }
    public int getAttackSpeed() {
        return attackSpeed;
    }
    public int getMunitions() {
        return munitions;
    }
    public int getMaxMunitions(){
        return maxMunition;
    }
}
