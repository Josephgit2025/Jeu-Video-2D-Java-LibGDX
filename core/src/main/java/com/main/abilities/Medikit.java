package com.main.abilities;

import com.main.effects.Heal;
import com.main.entities.player.Ability;

public class Medikit extends Ability{
    public Medikit(){
        super("Medikit", "Take a big shot of morphin to cope with the pain", 60, new Heal(150));
    }
}
