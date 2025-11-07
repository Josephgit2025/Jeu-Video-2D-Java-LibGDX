package com.main.effects;

public class DoDamage extends Effect{
    private final int damage;
    private final int range;

    public DoDamage(int damage, int range){
        super("doDamage", Effect.Type.DAMAGE);
        this.damage = damage;
        this.range = range;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }   
}
