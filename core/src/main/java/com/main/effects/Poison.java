package com.main.effects;

public class Poison extends Effect{
    private final int damagePerSecond;

    public Poison(int value){
        super("poison", Effect.Type.DOT);
        damagePerSecond = value;
    }

    public int getDamagePerSecond() {
        return damagePerSecond;
    }
}
