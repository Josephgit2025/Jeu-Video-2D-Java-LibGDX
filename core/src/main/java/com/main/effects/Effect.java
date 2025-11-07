package com.main.effects;


public abstract class Effect {
    protected String name;
    protected Type type;
    protected enum Type{
        BUFF,
        DEBUFF,
        AOE,
        DOT,
        DAMAGE
    }

    public Effect(String name, Type type){
        this.type = type;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Type getType(){
        return type;
    }
}
