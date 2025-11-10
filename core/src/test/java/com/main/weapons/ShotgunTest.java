package com.main.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

public class ShotgunTest {

    private Shotgun shotgun;

    @Before
    public void setUp() {
        shotgun = new Shotgun();
    }

    @Test
    public void testConstructor() {
        assertNotNull(shotgun);
        assertEquals(70, shotgun.getDamage());
        assertEquals(35, shotgun.getRange());
        assertEquals(4, shotgun.getAttackSpeed());
        assertEquals(6, shotgun.getMaxMunitions());
        assertEquals(6, shotgun.getMunitions());
    }

    @Test
    public void testAttack() {
        shotgun.attack();
        assertEquals(5, shotgun.getMunitions());
    }

    @Test
    public void testReload() {
        shotgun.attack();
        shotgun.attack();
        shotgun.reload();
        assertEquals(6, shotgun.getMunitions());
    }
}