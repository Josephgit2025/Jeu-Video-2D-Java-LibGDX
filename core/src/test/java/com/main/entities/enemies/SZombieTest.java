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

public class SZombieTest {
    
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
    public void testSZombieCreation() {
        SZombie szombie = new SZombie(100, 200);
        
        assertNotNull("SZombie should not be null", szombie);
        assertEquals(100.0f, szombie.getPosX(), 0.01f);
        assertEquals(200.0f, szombie.getPosY(), 0.01f);
    }
    
    @Test
    public void testSZombieHasHealth() {
        SZombie szombie = new SZombie(100, 200);
        
        assertTrue("SZombie should have positive health", szombie.getHealth() > 0);
    }
}