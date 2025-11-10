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

public class TankTest {

    private Tank tank;

    private class TestTank extends Tank {
        public TestTank(int posX, int posY) {
            super(null, posX, posY);
            this.texture = mock(Texture.class);
            this.sprite = mock(Sprite.class);
        }
    }

    @Before
    public void setUp() {
        tank = new TestTank(100, 200);
    }

    @Test
    public void testConstructor() {
        assertNotNull(tank);
        assertEquals(100, tank.getPosX(), 0.01f);
        assertEquals(200, tank.getPosY(), 0.01f);
    }

    @Test
    public void testMove() {
        float initialX = tank.getPosX();
        tank.move(1.0f); // delta = 1 seconde pour faciliter le calcul
        // déplacement = speed * delta = 15 * 1.0 = 15
        assertEquals(initialX + 15, tank.getPosX(), 0.01f);
    }

    @Test
    public void testMoveMultipleTimes() {
        float initialX = tank.getPosX();
        tank.move(1.0f);
        tank.move(1.0f);
        tank.move(1.0f);
        // déplacement total = 3 * (15 * 1.0) = 45
        assertEquals(initialX + 45, tank.getPosX(), 0.01f);
    }

    @Test
    public void testMoveWithRealisticDelta() {
        float initialX = tank.getPosX();
        tank.move(0.016f); // 60 FPS
        // déplacement = 15 * 0.016 = 0.24
        assertEquals(initialX + 0.24f, tank.getPosX(), 0.01f);
    }

    @Test
    public void testMoveWithZeroDelta() {
        float initialX = tank.getPosX();
        tank.move(0.0f);
        assertEquals(initialX, tank.getPosX(), 0.01f);
    }

    @Test
    public void testTakeDamage() {
        tank.takeDamage(250);
        assertFalse(tank.isDead());
        
        tank.takeDamage(250);
        assertTrue(tank.isDead());
    }

    @Test
    public void testTakeDamageExact() {
        tank.takeDamage(500);
        assertTrue(tank.isDead());
    }

    @Test
    public void testGetSprite() {
        assertNotNull(tank.getSprite());
    }
}