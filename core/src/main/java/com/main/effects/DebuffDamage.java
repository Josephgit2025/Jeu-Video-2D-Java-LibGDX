package com.main.effects;

public class DebuffDamage extends Effect{
    private final int debuffDamage;

    public DebuffDamage(int value){
        super("debufDamage", Effect.Type.DEBUFF);
        debuffDamage = value;
    }

    public int getDebuffDamage() {
        return debuffDamage;
    }
}
