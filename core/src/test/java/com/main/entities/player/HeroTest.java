package com.main.entities.player;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class HeroTest {
    
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
    public void testHeroCreation() {
        Hero hero = new Hero(100, 200);
        
        assertNotNull("Hero should not be null", hero);
        assertEquals(100.0f, hero.getPosX(), 0.01f);
        assertEquals(200.0f, hero.getPosY(), 0.01f);
        assertEquals(500, hero.getHealth());
    }
    
    @Test
    public void testHeroInitialWeapon() {
        Hero hero = new Hero(100, 200);
        
        assertNotNull("Hero should have a weapon", hero.getWeapon());
    }
    
    @Test
    public void testHeroTakeDamage() {
        Hero hero = new Hero(100, 200);
        int initialHealth = hero.getHealth();
        
        hero.takeDamage(50);
        
        assertEquals(initialHealth - 50, hero.getHealth());
    }
    
    @Test
    public void testHeroIsDead() {
        Hero hero = new Hero(100, 200);
        
        assertFalse("Hero should be alive initially", hero.isDead());
        
        hero.takeDamage(500);
        
        assertTrue("Hero should be dead after taking fatal damage", hero.isDead());
    }
    
    @Test
    public void testHeroHealthCannotBeNegative() {
        Hero hero = new Hero(100, 200);
        
        hero.takeDamage(1000);
        
        assertEquals(0, hero.getHealth());
    }
    
    @Test
    public void testHeroPosition() {
        Hero hero = new Hero(150.5f, 250.75f);
        
        assertEquals(150.5f, hero.getPosX(), 0.01f);
        assertEquals(250.75f, hero.getPosY(), 0.01f);
    }
}