package com.main.entities.enemies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class ZombieTest {

    private TestZombie zombie;

    // Classe pour tester sans charger de texture
    private class TestZombie extends Zombie {
        public TestZombie(float posX, float posY) {
            super(null, posX, posY);
            this.texture = mock(Texture.class);
            this.sprite = mock(Sprite.class);
        }
    }

    @Before
    public void setUp() {
        zombie = new TestZombie(100, 200);
    }

    @Test
    public void testConstructor() {
        assertNotNull(zombie);
        assertEquals(100, zombie.getPosX(), 0.01f);
        assertEquals(200, zombie.getPosY(), 0.01f);
    }

    @Test
    public void testInitialHealth() {
        assertFalse(zombie.isDead());
    }

    @Test
    public void testMove() {
        float initialX = zombie.getPosX();
        zombie.move();
        assertEquals(initialX - 2, zombie.getPosX(), 0.01f);
    }

    @Test
    public void testMoveMultipleTimes() {
        float initialX = zombie.getPosX();
        zombie.move();
        zombie.move();
        zombie.move();
        assertEquals(initialX - 6, zombie.getPosX(), 0.01f);
    }

    @Test
    public void testTakeDamage() {
        zombie.takeDamage(50);
        assertFalse(zombie.isDead());
        
        zombie.takeDamage(50);
        assertTrue(zombie.isDead());
    }

    @Test
    public void testGetSprite() {
        assertNotNull(zombie.getSprite());
    }
}