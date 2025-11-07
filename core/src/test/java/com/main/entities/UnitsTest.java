package com.main.entities.units;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UnitsTest {
    
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
    public void testSoldierCreation() {
        Soldier soldier = new Soldier(100, 200);
        
        assertNotNull("Soldier should not be null", soldier);
        assertEquals(100.0f, soldier.getPosX(), 0.01f);
        assertEquals(200.0f, soldier.getPosY(), 0.01f);
        assertTrue("Soldier should have positive health", soldier.getHealth() > 0);
    }
    
    @Test
    public void testSniperCreation() {
        Sniper sniper = new Sniper(150, 250);
        
        assertNotNull("Sniper should not be null", sniper);
        assertEquals(150.0f, sniper.getPosX(), 0.01f);
        assertEquals(250.0f, sniper.getPosY(), 0.01f);
        assertTrue("Sniper should have positive health", sniper.getHealth() > 0);
    }
    
    @Test
    public void testTankCreation() {
        Tank tank = new Tank(200, 300);
        
        assertNotNull("Tank should not be null", tank);
        assertTrue("Tank should have positive health", tank.getHealth() > 0);
    }
    
    @Test
    public void testMeleeCreation() {
        Melee melee = new Melee(50, 100);
        
        assertNotNull("Melee should not be null", melee);
        assertTrue("Melee should have positive health", melee.getHealth() > 0);
    }
    
    @Test
    public void testTankHasMoreHealthThanSoldier() {
        Tank tank = new Tank(100, 100);
        Soldier soldier = new Soldier(100, 100);
        
        assertTrue("Tank should have more health than Soldier", 
                   tank.getHealth() > soldier.getHealth());
    }
}