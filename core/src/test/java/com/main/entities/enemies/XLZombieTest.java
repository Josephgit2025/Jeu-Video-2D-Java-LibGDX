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

public class XLZombieTest {

    private TestXLZombie xlzombie;

    // Classe pour tester sans charger de texture
    private class TestXLZombie extends XLZombie {
        public TestXLZombie(int posX, int posY) {
            super(null, posX, posY);
            this.texture = mock(Texture.class);
            this.sprite = mock(Sprite.class);
        }
    }

    @Before
    public void setUp() {
        xlzombie = new TestXLZombie(100, 200);
    }

    @Test
    public void testConstructor() {
        assertNotNull(xlzombie);
        assertEquals(100, xlzombie.getPosX(), 0.01f);
        assertEquals(200, xlzombie.getPosY(), 0.01f);
    }

    @Test
    public void testInitialHealth() {
        assertFalse(xlzombie.isDead());
    }

    @Test
    public void testMove() {
        float initialX = xlzombie.getPosX();
        xlzombie.move(1.0f); // delta = 1 seconde
        // déplacement = speed * delta = 2 * 1.0 = 2
        assertEquals(initialX - 1, xlzombie.getPosX(), 0.01f);
    }

    @Test
    public void testMoveMultipleTimes() {
        float initialX = xlzombie.getPosX();
        xlzombie.move(1.0f);
        xlzombie.move(1.0f);
        xlzombie.move(1.0f);
        // déplacement total = 3 * (2 * 1.0) = 6
        assertEquals(initialX - 3, xlzombie.getPosX(), 0.01f);
    }

    @Test
    public void testMoveWithRealisticDelta() {
        float initialX = xlzombie.getPosX();
        xlzombie.move(0.016f); // 60 FPS
        // déplacement = 2 * 0.016 = 0.032
        assertEquals(initialX - 0.016f, xlzombie.getPosX(), 0.01f);
    }

    @Test
    public void testMoveWithZeroDelta() {
        float initialX = xlzombie.getPosX();
        xlzombie.move(0.0f);
        assertEquals(initialX, xlzombie.getPosX(), 0.01f);
    }

    @Test
    public void testTakeDamage() {
        xlzombie.takeDamage(250);
        assertFalse(xlzombie.isDead());
        
        xlzombie.takeDamage(250);
        assertTrue(xlzombie.isDead());
    }

    @Test
    public void testTakeDamageExact() {
        xlzombie.takeDamage(500);
        assertTrue(xlzombie.isDead());
    }

    @Test
    public void testGetSprite() {
        assertNotNull(xlzombie.getSprite());
    }
}