package com.main.abilities;

import com.main.effects.Poison;
import com.main.entities.player.Ability;

public class ThrowMolotov extends Ability{
    public ThrowMolotov(){
        super("Throw Molotov", "Throw a cocktail molotov at the designated area", 30, new Poison(20));
    }
}
