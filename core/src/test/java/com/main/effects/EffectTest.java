package com.main.effects;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.main.entities.player.Hero;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EffectTest {
    
    private static HeadlessApplication application;
    
    @BeforeClass
    public static void init() {
        application = new HeadlessApplication(new ApplicationAdapter() {});
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);
    }
    
    @AfterClass
    public static void cleanup() {
        if (application != null) {
            application.exit();
        }
    }
    
    @Test
    public void testHealEffect() {
        Hero hero = new Hero(100, 200);
        hero.takeDamage(100); // Reduce health
        int healthAfterDamage = hero.getHealth();
        
        Heal heal = new Heal(50);
        heal.apply(hero);
        
        assertEquals(healthAfterDamage + 50, hero.getHealth());
    }
    
    @Test
    public void testBuffDamageEffect() {
        BuffDamage buffDamage = new BuffDamage(10, 5.0f);
        
        assertNotNull("BuffDamage should not be null", buffDamage);
        assertEquals(10, buffDamage.getAmount());
        assertEquals(5.0f, buffDamage.getDuration(), 0.01f);
    }
    
    @Test
    public void testDebuffDamageEffect() {
        DebuffDamage debuffDamage = new DebuffDamage(5, 3.0f);
        
        assertNotNull("DebuffDamage should not be null", debuffDamage);
        assertEquals(5, debuffDamage.getAmount());
        assertEquals(3.0f, debuffDamage.getDuration(), 0.01f);
    }
    
    @Test
    public void testBuffMoveSpeedEffect() {
        BuffMoveSpeed buffSpeed = new BuffMoveSpeed(2, 4.0f);
        
        assertNotNull("BuffMoveSpeed should not be null", buffSpeed);
        assertEquals(2, buffSpeed.getAmount());
    }
    
    @Test
    public void testDebuffMoveSpeedEffect() {
        DebuffMoveSpeed debuffSpeed = new DebuffMoveSpeed(1, 2.0f);
        
        assertNotNull("DebuffMoveSpeed should not be null", debuffSpeed);
        assertEquals(1, debuffSpeed.getAmount());
    }
    
    @Test
    public void testPoisonEffect() {
        Poison poison = new Poison(5, 3.0f);
        
        assertNotNull("Poison should not be null", poison);
        assertTrue("Poison duration should be positive", poison.getDuration() > 0);
    }
    
    @Test
    public void testGoldMultiplierEffect() {
        GoldMultiplier goldMult = new GoldMultiplier(2.0f, 10.0f);
        
        assertNotNull("GoldMultiplier should not be null", goldMult);
        assertEquals(2.0f, goldMult.getMultiplier(), 0.01f);
    }
}