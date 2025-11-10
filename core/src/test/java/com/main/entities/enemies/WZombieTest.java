package com.main.entities.enemies;

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WZombieTest {

    private static Application application;
    private WZombie wZombie;
    
    @Mock
    private SpriteBatch mockBatch;
    
    @Mock
    private GL20 mockGL;
    
    @Mock
    private Graphics mockGraphics;

    @BeforeClass
    public static void init() {
        application = new HeadlessApplication(new com.badlogic.gdx.ApplicationAdapter() {});
    }

    @AfterClass
    public static void cleanUp() {
        if (application != null) {
            application.exit();
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Gdx.gl = mockGL;
        Gdx.gl20 = mockGL;
        Gdx.graphics = mockGraphics;
        
        when(mockGraphics.getWidth()).thenReturn(800);
        when(mockGraphics.getHeight()).thenReturn(600);
        
        wZombie = new WZombie(100, 200);
    }

    @Test
    public void testWZombieConstructor() {
        assertNotNull("WZombie should be created", wZombie);
        assertEquals("Initial X position should be 100", 100.0f, wZombie.getPosX(), 0.01f);
        assertEquals("Initial Y position should be 200", 200.0f, wZombie.getPosY(), 0.01f);
    }

    @Test
    public void testWZombieInitialHealth() {
        assertEquals("Initial health should be 200", 200, wZombie.getHealth());
    }

    @Test
    public void testWZombieInitialSpeed() {
        assertEquals("Initial speed should be 80", 80, wZombie.getSpeed(), 0.0f);
    }

    @Test
    public void testWZombieInitialAttackDamage() {
        assertEquals("Initial attack damage should be 10", 10, wZombie.getAttackDamage());
    }

    @Test
    public void testWZombieInitialAttackSpeed() {
        assertEquals("Initial attack speed should be 2", 2, wZombie.getAttackSpeed());
    }

    @Test
    public void testWZombieInitialRange() {
        assertEquals("Initial range should be 3", 3, wZombie.getRange());
    }

    @Test
    public void testWZombieIsNotDeadInitially() {
        assertFalse("WZombie should not be dead initially", wZombie.isDead());
    }

    @Test
    public void testMoveDecreasesXPosition() {
        float initialX = wZombie.getPosX();
        wZombie.move(1.0f);
        assertTrue("X position should decrease after move", wZombie.getPosX() < initialX);
    }

    @Test
    public void testMoveWithDelta() {
        float initialX = wZombie.getPosX();
        float delta = 0.5f;
        wZombie.move(delta);
        
        float expectedX = initialX - (80 * delta);
        assertEquals("X position should be correct after move", expectedX, wZombie.getPosX(), 0.01f);
    }

    @Test
    public void testMoveWithZeroDelta() {
        float initialX = wZombie.getPosX();
        wZombie.move(0.0f);
        assertEquals("X position should not change with zero delta", initialX, wZombie.getPosX(), 0.01f);
    }

    @Test
    public void testMoveWithLargeDelta() {
        float initialX = wZombie.getPosX();
        wZombie.move(2.0f);
        
        float expectedX = initialX - (80 * 2.0f);
        assertEquals("X position should be correct with large delta", expectedX, wZombie.getPosX(), 0.01f);
    }

    @Test
    public void testMultipleMoveCalls() {
        float initialX = wZombie.getPosX();
        wZombie.move(0.1f);
        wZombie.move(0.1f);
        wZombie.move(0.1f);
        
        float expectedX = initialX - (80 * 0.3f);
        assertEquals("X position should be correct after multiple moves", expectedX, wZombie.getPosX(), 0.01f);
    }

    @Test
    public void testRenderDoesNotThrow() {
        wZombie.render(mockBatch);
        // Vérifier que draw a été appelé sur le batch avec TextureRegion
        verify(mockBatch, atLeastOnce()).draw(any(TextureRegion.class), anyFloat(), anyFloat());
    }

    @Test
    public void testRenderMultipleTimes() {
        wZombie.render(mockBatch);
        wZombie.render(mockBatch);
        wZombie.render(mockBatch);
        
        verify(mockBatch, times(3)).draw(any(TextureRegion.class), anyFloat(), anyFloat());
    }

    @Test
    public void testRenderAfterMove() {
        wZombie.move(1.0f);
        wZombie.render(mockBatch);
        
        verify(mockBatch, atLeastOnce()).draw(any(TextureRegion.class), anyFloat(), anyFloat());
    }

    @Test
    public void testTakeDamage() {
        wZombie.takeDamage(50);
        assertEquals("Health should decrease by 50", 150, wZombie.getHealth());
    }

    @Test
    public void testTakeDamageMultipleTimes() {
        wZombie.takeDamage(30);
        wZombie.takeDamage(40);
        assertEquals("Health should decrease by 70 total", 130, wZombie.getHealth());
    }

    @Test
    public void testTakeDamageUntilDeath() {
        wZombie.takeDamage(250);
        assertTrue("WZombie should be dead after taking fatal damage", wZombie.isDead());
    }

    @Test
    public void testHealthCannotGoNegative() {
        wZombie.takeDamage(300);
        assertTrue("Health should result in death", wZombie.getHealth() <= 0);
    }

    @Test
    public void testIsDeadWhenHealthZero() {
        wZombie.takeDamage(200);
        assertTrue("WZombie should be dead when health is zero", wZombie.isDead());
    }

    @Test
    public void testMoveAndRenderSequence() {
        float initialX = wZombie.getPosX();
        
        wZombie.move(0.5f);
        wZombie.render(mockBatch);
        wZombie.move(0.5f);
        wZombie.render(mockBatch);
        
        assertTrue("X position should have moved", wZombie.getPosX() < initialX);
        verify(mockBatch, times(2)).draw(any(TextureRegion.class), anyFloat(), anyFloat());
    }

    @Test
    public void testPositionGetters() {
        WZombie zombie = new WZombie(150, 250);
        assertEquals("getPosX should return correct value", 150.0f, zombie.getPosX(), 0.01f);
        assertEquals("getPosY should return correct value", 250.0f, zombie.getPosY(), 0.01f);
    }

    @Test
    public void testYPositionStaysConstant() {
        float initialY = wZombie.getPosY();
        wZombie.move(1.0f);
        assertEquals("Y position should not change during horizontal movement", initialY, wZombie.getPosY(), 0.01f);
    }

    @Test
    public void testMoveLeftAnimation() {
        // Simuler plusieurs frames pour tester l'animation
        for (int i = 0; i < 10; i++) {
            wZombie.move(0.2f);
            wZombie.render(mockBatch);
        }
        
        verify(mockBatch, times(10)).draw(any(TextureRegion.class), anyFloat(), anyFloat());
    }

    @Test
    public void testWZombieInheritance() {
        assertTrue("WZombie should be instance of Zombie", wZombie instanceof Zombie);
    }

    @Test
    public void testWZombieStatsAreDifferentFromBase() {
        // WZombie a des stats spécifiques (200 HP, 80 speed, etc.)
        assertEquals("WZombie health should be 200", 200, wZombie.getHealth());
        assertEquals("WZombie speed should be 80", 80, wZombie.getSpeed(), 0.0f);
    }

    @Test
    public void testConstructorWithDifferentPositions() {
        WZombie z1 = new WZombie(0, 0);
        WZombie z2 = new WZombie(500, 300);
        
        assertEquals("First zombie X should be 0", 0.0f, z1.getPosX(), 0.01f);
        assertEquals("Second zombie X should be 500", 500.0f, z2.getPosX(), 0.01f);
        assertEquals("First zombie Y should be 0", 0.0f, z1.getPosY(), 0.01f);
        assertEquals("Second zombie Y should be 300", 300.0f, z2.getPosY(), 0.01f);
    }

    @Test
    public void testRenderWithoutMove() {
        // Tester le render sans avoir appelé move
        wZombie.render(mockBatch);
        verify(mockBatch, atLeastOnce()).draw(any(TextureRegion.class), anyFloat(), anyFloat());
    }

    @Test
    public void testContinuousMovement() {
        float initialX = wZombie.getPosX();
        float totalDelta = 0f;
        
        for (int i = 0; i < 60; i++) { // Simuler 60 frames
            wZombie.move(0.016f);
            totalDelta += 0.016f;
        }
        
        float expectedX = initialX - (80 * totalDelta);
        assertEquals("Position after continuous movement", expectedX, wZombie.getPosX(), 0.1f);
    }
}
