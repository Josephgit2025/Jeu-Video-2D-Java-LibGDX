package com.main.abilities;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class AbilitiesTest {
    
    @Test
    public void testDopingProductsCreation() {
        DopingProducts doping = new DopingProducts();
        
        assertNotNull("DopingProducts should not be null", doping);
        assertTrue("DopingProducts cooldown should be positive", doping.getCooldown() > 0);
    }
    
    @Test
    public void testIncendiaryRoundsCreation() {
        IncendiaryRounds incendiary = new IncendiaryRounds();
        
        assertNotNull("IncendiaryRounds should not be null", incendiary);
        assertTrue("IncendiaryRounds cooldown should be positive", incendiary.getCooldown() > 0);
    }
    
    @Test
    public void testMedikitCreation() {
        Medikit medikit = new Medikit();
        
        assertNotNull("Medikit should not be null", medikit);
        assertTrue("Medikit cooldown should be positive", medikit.getCooldown() > 0);
    }
    
    @Test
    public void testMidasTouchCreation() {
        MidasTouch midas = new MidasTouch();
        
        assertNotNull("MidasTouch should not be null", midas);
        assertTrue("MidasTouch cooldown should be positive", midas.getCooldown() > 0);
    }
    
    @Test
    public void testThrowMolotovCreation() {
        ThrowMolotov molotov = new ThrowMolotov();
        
        assertNotNull("ThrowMolotov should not be null", molotov);
        assertTrue("ThrowMolotov cooldown should be positive", molotov.getCooldown() > 0);
    }
    
    @Test
    public void testWarCryCreation() {
        WarCry warCry = new WarCry();
        
        assertNotNull("WarCry should not be null", warCry);
        assertTrue("WarCry cooldown should be positive", warCry.getCooldown() > 0);
    }
}