<<<<<<< HEAD:game/src/main/java/com/main/entities/enemies/Zombie.java
package com.main.entities.enemies;

import com.main.entities.Unit;

public class Zombie extends Unit {

    public Zombie (String filePath, int posX, int posY) {
        super(filePath, posX, posY);
        this.health = 100;
        this.attackDamage = 15;
        this.speed = 2;
        this.attackSpeed = 1;
        this.range = 100;
    }

}
    
=======
package com.main.entities.enemies;

import com.main.entities.Unit;

public class Zombie extends Unit {

    public Zombie (String filePath, float posX, float posY) {
        super(filePath, posX, posY);
        this.health = 100;
        this.attackDamage = 15;
        this.speed = 2;
        this.attackSpeed = 1;
        this.range = 100;
    }

}
>>>>>>> feature/migration:core/src/main/java/com/main/entities/enemies/Zombie.java
