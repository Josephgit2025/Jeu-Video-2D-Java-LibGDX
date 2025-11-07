package com.main.abilities;

import com.main.effects.DoDamageAOE;
import com.main.entities.player.Ability;

public class IncendiaryRounds extends Ability{
    public IncendiaryRounds(){
        super("Incendiary Rounds", "A brand new model of bullets, making them explosives !", 90, new DoDamageAOE(30, 50));
    }
}
