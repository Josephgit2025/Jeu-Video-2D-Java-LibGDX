package com.main.effects;

public class DebuffMoveSpeed extends Effect{
    private final int debuffMoveSpeedValue;
    
    public DebuffMoveSpeed(int value){
        super("debuffMoveSpeed", Effect.Type.DEBUFF);
        debuffMoveSpeedValue = value;
    }

    public int getDebuffMoveSpeedValue(){
        return debuffMoveSpeedValue;
    }
}
