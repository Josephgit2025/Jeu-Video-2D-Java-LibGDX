<<<<<<< HEAD:game/src/main/java/com/main/entities/units/Sniper.java
package com.main.entities.units;

public class Sniper extends Soldier{

    public Sniper (int posX, int posY) {
        super("/com/main/assets/Sniper.png", posX, posY);
        this.health = 150;
        this.attackDamage = 40;
        this.speed = 4;
        this.attackSpeed = 3;
        this.range = 400;
    }
}
 
=======
package com.main.entities.units;

public class Sniper extends Soldier{

    public Sniper (float posX, float posY) {
        super("units/Sniper.png", posX, posY);
        this.health = 150;
        this.attackDamage = 40;
        this.speed = 4;
        this.attackSpeed = 3;
        this.range = 400;
    }
}
>>>>>>> feature/migration:core/src/main/java/com/main/entities/units/Sniper.java
