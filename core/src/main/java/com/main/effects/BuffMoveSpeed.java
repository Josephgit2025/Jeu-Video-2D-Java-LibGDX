package com.main.effects;

public class BuffMoveSpeed extends Effect{
    private final int buffMoveSpeedValue;
    
    public BuffMoveSpeed(int value){
        super("buffMoveSpeed", Effect.Type.DEBUFF);
        buffMoveSpeedValue = value;
    }

    public int getBuffMoveSpeedValue(){
        return buffMoveSpeedValue;
    }
}