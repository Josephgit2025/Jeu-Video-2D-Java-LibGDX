package com.main.effects;

public class Heal extends Effect{
    private final int regen;

    public Heal(int value){
        super("regen", Effect.Type.BUFF);
        regen = value;
    }

    public int getRegen() {
        return regen;
    }
}
