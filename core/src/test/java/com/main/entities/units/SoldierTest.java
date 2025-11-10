package com.main.entities.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SoldierTest {

    private Soldier soldier;

    private class TestSoldier extends Soldier {
        public TestSoldier(float posX, float posY) {
            super(null, posX, posY);
            this.texture = mock(Texture.class);
            this.sprite = mock(Sprite.class);
            this.speed = 2; // Vitesse du zombie de base
        }
    }

    @Before
    public void setUp() {
        soldier = new TestSoldier(100, 200);
    }

    @Test
    public void testConstructor() {
        assertNotNull(soldier);
        assertEquals(100, soldier.getPosX(), 0.01f);
        assertEquals(200, soldier.getPosY(), 0.01f);
    }

    @Test
    public void testMove() {
        // Soldier ne définit pas de speed par défaut, donc on le set
        float initialX = soldier.getPosX();
        soldier.move(0.016f);
        // Le mouvement dépend de la vitesse qui n'est pas définie dans Soldier de base
        assertTrue(soldier.getPosX() >= initialX);
    }

    @Test
    public void testGetSprite() {
        assertNotNull(soldier.getSprite());
    }
}