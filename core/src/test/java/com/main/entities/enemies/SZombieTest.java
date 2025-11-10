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

public class SZombieTest {

    private TestSZombie szombie;

    // Classe pour tester sans charger de texture
    private class TestSZombie extends SZombie {
        public TestSZombie(int posX, int posY) {
            super(null, posX, posY);
            this.texture = mock(Texture.class);
            this.sprite = mock(Sprite.class);
        }
    }

    @Before
    public void setUp() {
        szombie = new TestSZombie(100, 200);
    }

    @Test
    public void testConstructor() {
        assertNotNull(szombie);
        assertEquals(100, szombie.getPosX(), 0.01f);
        assertEquals(200, szombie.getPosY(), 0.01f);
    }

    @Test
    public void testInitialHealth() {
        assertFalse(szombie.isDead());
    }

    @Test
    public void testMove() {
        float initialX = szombie.getPosX();
        szombie.move();
        assertEquals(initialX - 2, szombie.getPosX(), 0.01f);
    }

    @Test
    public void testMoveMultipleTimes() {
        float initialX = szombie.getPosX();
        szombie.move();
        szombie.move();
        szombie.move();
        assertEquals(initialX - 6, szombie.getPosX(), 0.01f);
    }

    @Test
    public void testTakeDamage() {
        szombie.takeDamage(100);
        assertFalse(szombie.isDead());
        
        szombie.takeDamage(100);
        assertTrue(szombie.isDead());
    }

    @Test
    public void testTakeDamageExact() {
        szombie.takeDamage(200);
        assertTrue(szombie.isDead());
    }

    @Test
    public void testGetSprite() {
        assertNotNull(szombie.getSprite());
    }
}