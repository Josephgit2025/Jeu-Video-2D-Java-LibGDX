package com.main.entities.enemies;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class XLZombieTest {
    
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
    public void testXLZombieCreation() {
        XLZombie xlzombie = new XLZombie(100, 200);
        
        assertNotNull("XLZombie should not be null", xlzombie);
        assertEquals(100.0f, xlzombie.getPosX(), 0.01f);
        assertEquals(200.0f, xlzombie.getPosY(), 0.01f);
    }
    
    @Test
    public void testXLZombieHasMoreHealthThanRegularZombie() {
        XLZombie xlzombie = new XLZombie(100, 200);
        Zombie zombie = new Zombie(100, 200);
        
        assertTrue("XLZombie should have more health than regular zombie", 
                   xlzombie.getHealth() > zombie.getHealth());
    }
}