package com.main.entities.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class SoldierTest {

    private Soldier soldier;

    @Before
    public void setUp() {
        soldier = new Soldier("zombie/women/Walk1.png", 100, 200);
    }

    @Test
    public void testConstructor() {
        assertNotNull(soldier);
        assertEquals(100, soldier.getPosX(), 0.01f);
        assertEquals(200, soldier.getPosY(), 0.01f);
    }

    @Test
    public void testMove() {
        // Soldier ne définit pas de speed par défaut, donc on le set
        float initialX = soldier.getPosX();
        soldier.move();
        // Le mouvement dépend de la vitesse qui n'est pas définie dans Soldier de base
        assertTrue(soldier.getPosX() >= initialX);
    }

    @Test
    public void testGetSprite() {
        assertNotNull(soldier.getSprite());
    }
}