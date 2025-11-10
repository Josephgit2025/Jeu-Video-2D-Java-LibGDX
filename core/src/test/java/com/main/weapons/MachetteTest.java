package com.main.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

public class MachetteTest {

    private Machette machette;

    @Before
    public void setUp() {
        machette = new Machette();
    }

    @Test
    public void testConstructor() {
        assertNotNull(machette);
        assertEquals(5, machette.getDamage());
        assertEquals(25, machette.getRange());
        assertEquals(-1, machette.getAttackSpeed());
        assertEquals(-1, machette.getMaxMunitions());
        assertEquals(-1, machette.getMunitions());
    }

    @Test
    public void testAttackNoMunitionDecrease() {
        int initialMunitions = machette.getMunitions();
        machette.attack();
        // La machette a des munitions illimitées (-1), donc pas de décrémentation
        assertEquals(initialMunitions, machette.getMunitions());
    }

    @Test
    public void testReload() {
        machette.reload();
        assertEquals(-1, machette.getMunitions());
    }
}