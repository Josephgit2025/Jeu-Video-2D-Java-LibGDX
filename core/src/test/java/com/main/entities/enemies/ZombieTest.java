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

public class ZombieTest {
    
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
    public void testZombieCreation() {
        Zombie zombie = new Zombie(100, 200);
        
        assertNotNull("Zombie should not be null", zombie);
        assertEquals(100.0f, zombie.getPosX(), 0.01f);
        assertEquals(200.0f, zombie.getPosY(), 0.01f);
    }
    
    @Test
    public void testZombieHasHealth() {
        Zombie zombie = new Zombie(100, 200);
        
        assertTrue("Zombie should have positive health", zombie.getHealth() > 0);
    }
    
    @Test
    public void testZombieTakeDamage() {
        Zombie zombie = new Zombie(100, 200);
        int initialHealth = zombie.getHealth();
        
        zombie.takeDamage(20);
        
        assertEquals(initialHealth - 20, zombie.getHealth());
    }
    
    @Test
    public void testZombieAttack() {
        Zombie zombie = new Zombie(100, 200);
        
        // Test que la méthode attack ne plante pas
        assertNotNull(zombie);
        zombie.attack();
    }
}