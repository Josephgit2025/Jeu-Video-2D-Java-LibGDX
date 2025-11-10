package com.main.entities.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class SniperTest {

    private Sniper sniper;

    @Before
    public void setUp() {
        sniper = new Sniper(100, 200);
    }

    @Test
    public void testConstructor() {
        assertNotNull(sniper);
        assertEquals(100, sniper.getPosX(), 0.01f);
        assertEquals(200, sniper.getPosY(), 0.01f);
    }

    @Test
    public void testMove() {
        float initialX = sniper.getPosX();
        sniper.move();
        assertEquals(initialX + 30, sniper.getPosX(), 0.01f);
    }

    @Test
    public void testMoveMultipleTimes() {
        float initialX = sniper.getPosX();
        sniper.move();
        sniper.move();
        sniper.move();
        assertEquals(initialX + 90, sniper.getPosX(), 0.01f);
    }

    @Test
    public void testTakeDamage() {
        sniper.takeDamage(75);
        assertFalse(sniper.isDead());
        
        sniper.takeDamage(75);
        assertTrue(sniper.isDead());
    }

    @Test
    public void testTakeDamageExact() {
        sniper.takeDamage(150);
        assertTrue(sniper.isDead());
    }

    @Test
    public void testGetSprite() {
        assertNotNull(sniper.getSprite());
    }
}