package com.main.entities.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MeleeTest {

    private Melee melee;

    private class TestMelee extends Melee {
        public TestMelee(int posX, int posY) {
            super(null, posX, posY);
            this.texture = mock(Texture.class);
            this.sprite = mock(Sprite.class);
        }
    }

    @Before
    public void setUp() {
        melee = new TestMelee(100, 200);
    }

    @Test
    public void testConstructor() {
        assertNotNull(melee);
        assertEquals(100, melee.getPosX(), 0.01f);
        assertEquals(200, melee.getPosY(), 0.01f);
    }

    @Test
    public void testMove() {
        float initialX = melee.getPosX();
        melee.move(1.0f); // delta = 1 seconde
        // déplacement = speed * delta = 45 * 1.0 = 45
        assertEquals(initialX + 45, melee.getPosX(), 0.01f);
    }

    @Test
    public void testMoveMultipleTimes() {
        float initialX = melee.getPosX();
        melee.move(1.0f);
        melee.move(1.0f);
        melee.move(1.0f);
        // déplacement total = 3 * (45 * 1.0) = 135
        assertEquals(initialX + 135, melee.getPosX(), 0.01f);
    }

    @Test
    public void testMoveWithRealisticDelta() {
        float initialX = melee.getPosX();
        melee.move(0.016f); // 60 FPS
        // déplacement = 45 * 0.016 = 0.72
        assertEquals(initialX + 0.72f, melee.getPosX(), 0.01f);
    }

    @Test
    public void testMoveWithZeroDelta() {
        float initialX = melee.getPosX();
        melee.move(0.0f);
        assertEquals(initialX, melee.getPosX(), 0.01f);
    }

    @Test
    public void testTakeDamage() {
        melee.takeDamage(100);
        assertFalse(melee.isDead());
        
        melee.takeDamage(100);
        assertTrue(melee.isDead());
    }

    @Test
    public void testTakeDamageExact() {
        melee.takeDamage(200);
        assertTrue(melee.isDead());
    }

    @Test
    public void testGetSprite() {
        assertNotNull(melee.getSprite());
    }
}