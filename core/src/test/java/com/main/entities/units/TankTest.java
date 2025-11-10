package com.main.entities.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TankTest {

    private Tank tank;

    @Before
    public void setUp() {
        tank = new Tank(100, 200);
    }

    @Test
    public void testConstructor() {
        assertNotNull(tank);
        assertEquals(100, tank.getPosX(), 0.01f);
        assertEquals(200, tank.getPosY(), 0.01f);
    }

    @Test
    public void testMove() {
        float initialX = tank.getPosX();
        tank.move();
        assertEquals(initialX + 15, tank.getPosX(), 0.01f);
    }

    @Test
    public void testMoveMultipleTimes() {
        float initialX = tank.getPosX();
        tank.move();
        tank.move();
        tank.move();
        assertEquals(initialX + 45, tank.getPosX(), 0.01f);
    }

    @Test
    public void testTakeDamage() {
        tank.takeDamage(250);
        assertFalse(tank.isDead());
        
        tank.takeDamage(250);
        assertTrue(tank.isDead());
    }

    @Test
    public void testTakeDamageExact() {
        tank.takeDamage(500);
        assertTrue(tank.isDead());
    }

    @Test
    public void testGetSprite() {
        assertNotNull(tank.getSprite());
    }
}