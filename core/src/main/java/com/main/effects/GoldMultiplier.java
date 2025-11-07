package com.main.effects;

public class GoldMultiplier extends Effect{
    private final int multiplier;

    public GoldMultiplier(int value){
        super("goldMultiplier", Effect.Type.BUFF);
        multiplier = value;
    }

    public int getMultiplier() {
        return multiplier;
    }
}
