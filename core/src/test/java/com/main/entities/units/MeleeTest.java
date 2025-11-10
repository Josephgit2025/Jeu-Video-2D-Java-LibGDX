package com.main.entities.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class MeleeTest {

    private Melee melee;

    @Before
    public void setUp() {
        melee = new Melee(100, 200);
    }

    @Test
    public void testConstructor() {
        assertNotNull(melee);
        assertEquals(100, melee.getPosX(), 0.01f);
        assertEquals(200, melee.getPosY(), 0.01f);
    }

    @Test
    public void testMove() {
        float initialX = melee.getPosX();
        melee.move();
        assertEquals(initialX + 45, melee.getPosX(), 0.01f);
    }

    @Test
    public void testMoveMultipleTimes() {
        float initialX = melee.getPosX();
        melee.move();
        melee.move();
        melee.move();
        assertEquals(initialX + 135, melee.getPosX(), 0.01f);
    }

    @Test
    public void testTakeDamage() {
        melee.takeDamage(100);
        assertFalse(melee.isDead());
        
        melee.takeDamage(100);
        assertTrue(melee.isDead());
    }

    @Test
    public void testTakeDamageExact() {
        melee.takeDamage(200);
        assertTrue(melee.isDead());
    }

    @Test
    public void testGetSprite() {
        assertNotNull(melee.getSprite());
    }
}