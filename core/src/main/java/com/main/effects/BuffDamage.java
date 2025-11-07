package com.main.effects;

public class BuffDamage extends Effect {
    private final int boostDamageValue;

    public BuffDamage(int value){
        super("buffDamage", Effect.Type.BUFF);
        boostDamageValue = value;
    }

    public int getBoostDamageValue(){
        return boostDamageValue;
    }
}
