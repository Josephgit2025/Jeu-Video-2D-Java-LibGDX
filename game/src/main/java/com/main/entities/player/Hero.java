package com.main.entities.player;
import com.main.entities.Unit;
import com.main.entities.player.*;
import java.util.List;
import java.util.ArrayList;
import com.main.weapons.*;

public class Hero extends Unit {

    protected int xp;
    protected int level;
    protected List<Ability> abilities = new ArrayList<>();
    protected int strength;
    protected int dexterity;
    protected int agility; 
    protected Weapon weapon;
   

    public Hero(int posX, int posY) {
        super("/com/main/assets/Hero.png", posX, posY);
        this.health = 500;
        this.weapon = new Machette();
        this.speed = 3;
        this.attackSpeed = 1;
    }

}
 