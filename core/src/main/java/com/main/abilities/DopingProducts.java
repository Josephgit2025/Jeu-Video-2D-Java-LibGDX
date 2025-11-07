package com.main.abilities;

import com.main.effects.BuffMoveSpeed;
import com.main.entities.player.Ability;

public class DopingProducts extends Ability{
    public DopingProducts(){
        super("Steroids", "Inject chemicals into your allies to buff their movement speed", 30, new BuffMoveSpeed(30));
    }
}
