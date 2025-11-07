package com.main.weapons;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class WeaponTest {
    
    @Test
    public void testMachetteCreation() {
        Machette machette = new Machette();
        
        assertNotNull("Machette should not be null", machette);
        assertTrue("Machette damage should be positive", machette.getDamage() > 0);
    }
    
    @Test
    public void testPistolCreation() {
        Pistol pistol = new Pistol();
        
        assertNotNull("Pistol should not be null", pistol);
        assertTrue("Pistol damage should be positive", pistol.getDamage() > 0);
    }
    
    @Test
    public void testAssaultRifleCreation() {
        AssaultRifle ar = new AssaultRifle();
        
        assertNotNull("AssaultRifle should not be null", ar);
        assertTrue("AssaultRifle damage should be positive", ar.getDamage() > 0);
    }
    
    @Test
    public void testShotgunCreation() {
        Shotgun shotgun = new Shotgun();
        
        assertNotNull("Shotgun should not be null", shotgun);
        assertTrue("Shotgun damage should be positive", shotgun.getDamage() > 0);
    }
    
    @Test
    public void testSMGCreation() {
        SMG smg = new SMG();
        
        assertNotNull("SMG should not be null", smg);
        assertTrue("SMG damage should be positive", smg.getDamage() > 0);
    }
    
    @Test
    public void testSniperRifleCreation() {
        SniperRifle sniper = new SniperRifle();
        
        assertNotNull("SniperRifle should not be null", sniper);
        assertTrue("SniperRifle damage should be positive", sniper.getDamage() > 0);
    }
    
    @Test
    public void testSniperRifleHasHigherDamageThanPistol() {
        SniperRifle sniper = new SniperRifle();
        Pistol pistol = new Pistol();
        
        assertTrue("SniperRifle should have higher damage than Pistol", 
                   sniper.getDamage() > pistol.getDamage());
    }
}