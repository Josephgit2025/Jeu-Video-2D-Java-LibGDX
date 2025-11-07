package com.main.effects;

public class DoDamageAOE extends Effect{
    private final int damage;
    private final int aoe;

    public DoDamageAOE(int damage, int aoe){
        super("doDamageAOE", Effect.Type.AOE);
        this.damage = damage;
        this.aoe = aoe;
    }

    public int getDamage() {
        return damage;
    }

    public int getAoe() {
        return aoe;
    }
}
