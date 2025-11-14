package com.ui;

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
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class goldTest {

    private static Application application;
    private gold goldDisplay;
    
    @Mock
    private SpriteBatch mockBatch;
    
    @Mock
    private ShapeRenderer mockShapeRenderer;
    
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
        
        goldDisplay = new gold(100, 200);
    }

    // ===== Constructor Tests =====

    @Test
    public void testConstructorWithoutIcon() {
        assertNotNull("GoldDisplay should not be null", goldDisplay);
        assertEquals("Initial gold should be 0", 0, goldDisplay.getGold());
    }

    @Test
    public void testConstructorWithIcon() {
        // Constructor with icon path - will fail to load in headless mode but shouldn't crash
        gold goldWithIcon = new gold(150, 250, "coin.png");
        assertNotNull("GoldDisplay with icon should not be null", goldWithIcon);
        assertEquals("Initial gold should be 0", 0, goldWithIcon.getGold());
    }

    @Test
    public void testConstructorWithDifferentPositions() {
        gold gold1 = new gold(0, 0);
        gold gold2 = new gold(500, 300);
        
        assertNotNull("First gold display should not be null", gold1);
        assertNotNull("Second gold display should not be null", gold2);
    }

    @Test
    public void testConstructorWithInvalidIconPath() {
        gold goldWithInvalidIcon = new gold(100, 200, "invalid/path/coin.png");
        assertNotNull("GoldDisplay should handle invalid icon path", goldWithInvalidIcon);
        assertEquals("Initial gold should still be 0", 0, goldWithInvalidIcon.getGold());
    }

    // ===== Gold Value Tests =====

    @Test
    public void testGetGoldInitial() {
        assertEquals("Initial gold should be 0", 0, goldDisplay.getGold());
    }

    @Test
    public void testUpdateGold() {
        goldDisplay.update(100);
        assertEquals("Gold should be updated to 100", 100, goldDisplay.getGold());
    }

    @Test
    public void testUpdateGoldMultipleTimes() {
        goldDisplay.update(50);
        assertEquals("Gold should be 50", 50, goldDisplay.getGold());
        
        goldDisplay.update(150);
        assertEquals("Gold should be updated to 150", 150, goldDisplay.getGold());
    }

    @Test
    public void testUpdateGoldWithNegativeValue() {
        goldDisplay.update(-50);
        assertEquals("Negative gold should be clamped to 0", 0, goldDisplay.getGold());
    }

    @Test
    public void testUpdateGoldWithZero() {
        goldDisplay.update(100);
        goldDisplay.update(0);
        assertEquals("Gold should be 0", 0, goldDisplay.getGold());
    }

    @Test
    public void testSetGold() {
        goldDisplay.setGold(250);
        assertEquals("Gold should be set to 250", 250, goldDisplay.getGold());
    }

    @Test
    public void testSetGoldWithNegativeValue() {
        goldDisplay.setGold(-100);
        assertEquals("Negative gold should be clamped to 0", 0, goldDisplay.getGold());
    }

    @Test
    public void testSetGoldMultipleTimes() {
        goldDisplay.setGold(100);
        goldDisplay.setGold(200);
        goldDisplay.setGold(50);
        assertEquals("Gold should be 50", 50, goldDisplay.getGold());
    }

    @Test
    public void testAddGold() {
        goldDisplay.setGold(100);
        goldDisplay.addGold(50);
        assertEquals("Gold should be 150 after adding 50", 150, goldDisplay.getGold());
    }

    @Test
    public void testAddGoldMultipleTimes() {
        goldDisplay.addGold(10);
        goldDisplay.addGold(20);
        goldDisplay.addGold(30);
        assertEquals("Gold should be 60 after multiple additions", 60, goldDisplay.getGold());
    }

    @Test
    public void testAddNegativeGold() {
        goldDisplay.setGold(100);
        goldDisplay.addGold(-30);
        assertEquals("Gold should decrease to 70", 70, goldDisplay.getGold());
    }

    @Test
    public void testAddLargeAmount() {
        goldDisplay.addGold(1000000);
        assertEquals("Should handle large gold amounts", 1000000, goldDisplay.getGold());
    }

    @Test
    public void testRemoveGoldSuccess() {
        goldDisplay.setGold(100);
        boolean result = goldDisplay.removeGold(50);
        
        assertTrue("Remove should succeed", result);
        assertEquals("Gold should be 50 after removal", 50, goldDisplay.getGold());
    }

    @Test
    public void testRemoveGoldFailure() {
        goldDisplay.setGold(30);
        boolean result = goldDisplay.removeGold(50);
        
        assertFalse("Remove should fail when not enough gold", result);
        assertEquals("Gold should remain 30", 30, goldDisplay.getGold());
    }

    @Test
    public void testRemoveGoldExact() {
        goldDisplay.setGold(100);
        boolean result = goldDisplay.removeGold(100);
        
        assertTrue("Remove exact amount should succeed", result);
        assertEquals("Gold should be 0", 0, goldDisplay.getGold());
    }

    @Test
    public void testRemoveGoldMultipleTimes() {
        goldDisplay.setGold(100);
        
        assertTrue("First removal should succeed", goldDisplay.removeGold(20));
        assertEquals("Gold should be 80", 80, goldDisplay.getGold());
        
        assertTrue("Second removal should succeed", goldDisplay.removeGold(30));
        assertEquals("Gold should be 50", 50, goldDisplay.getGold());
        
        assertFalse("Third removal should fail", goldDisplay.removeGold(60));
        assertEquals("Gold should still be 50", 50, goldDisplay.getGold());
    }

    @Test
    public void testRemoveZeroGold() {
        goldDisplay.setGold(100);
        boolean result = goldDisplay.removeGold(0);
        
        assertTrue("Removing 0 gold should succeed", result);
        assertEquals("Gold should remain 100", 100, goldDisplay.getGold());
    }

    // ===== Position Tests =====

    @Test
    public void testSetPosition() {
        goldDisplay.setPosition(300, 400);
        // Position is private, but we can verify it doesn't crash
        assertNotNull("Gold display should still be valid", goldDisplay);
    }

    @Test
    public void testSetPositionMultipleTimes() {
        goldDisplay.setPosition(100, 100);
        goldDisplay.setPosition(200, 200);
        goldDisplay.setPosition(50, 50);
        assertNotNull("Gold display should handle position changes", goldDisplay);
    }

    @Test
    public void testSetPositionNegativeValues() {
        goldDisplay.setPosition(-50, -100);
        assertNotNull("Gold display should handle negative positions", goldDisplay);
    }

    @Test
    public void testSetPositionZero() {
        goldDisplay.setPosition(0, 0);
        assertNotNull("Gold display should handle zero position", goldDisplay);
    }

    // ===== Render Tests =====

    @Test
    public void testRenderWithBatch() {
        goldDisplay.render(mockBatch);
        verify(mockBatch, atLeastOnce()).begin();
        verify(mockBatch, atLeastOnce()).end();
    }

    @Test
    public void testRenderWithGoldValue() {
        goldDisplay.setGold(100);
        goldDisplay.render(mockBatch);
        verify(mockBatch, atLeastOnce()).begin();
        verify(mockBatch, atLeastOnce()).end();
    }

    @Test
    public void testRenderMultipleTimes() {
        goldDisplay.render(mockBatch);
        goldDisplay.render(mockBatch);
        goldDisplay.render(mockBatch);
        verify(mockBatch, atLeastOnce()).begin();
        verify(mockBatch, atLeastOnce()).end();
    }

    @Test
    public void testRenderWithBackground() {
        goldDisplay.setGold(500);
        goldDisplay.render(mockShapeRenderer, mockBatch);
        
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
        verify(mockShapeRenderer, atLeastOnce()).end();
        verify(mockBatch, atLeastOnce()).begin();
        verify(mockBatch, atLeastOnce()).end();
    }

    @Test
    public void testRenderWithBackgroundMultipleTimes() {
        goldDisplay.render(mockShapeRenderer, mockBatch);
        goldDisplay.render(mockShapeRenderer, mockBatch);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
        verify(mockShapeRenderer, atLeastOnce()).end();
    }

    @Test
    public void testRenderAfterUpdate() {
        goldDisplay.update(250);
        goldDisplay.render(mockBatch);
        verify(mockBatch, atLeastOnce()).begin();
    }

    @Test
    public void testRenderAfterPositionChange() {
        goldDisplay.setPosition(500, 500);
        goldDisplay.render(mockBatch);
        verify(mockBatch, atLeastOnce()).begin();
    }

    // ===== Visual Customization Tests =====

    @Test
    public void testSetColor() {
        Color customColor = new Color(1f, 0f, 0f, 1f);
        goldDisplay.setColor(customColor);
        assertNotNull("Gold display should handle color change", goldDisplay);
    }

    @Test
    public void testSetColorMultipleTimes() {
        goldDisplay.setColor(Color.RED);
        goldDisplay.setColor(Color.BLUE);
        goldDisplay.setColor(Color.GREEN);
        assertNotNull("Gold display should handle multiple color changes", goldDisplay);
    }

    @Test
    public void testSetFontScale() {
        goldDisplay.setFontScale(2.0f);
        assertNotNull("Gold display should handle font scale change", goldDisplay);
    }

    @Test
    public void testSetFontScaleMultipleTimes() {
        goldDisplay.setFontScale(1.0f);
        goldDisplay.setFontScale(2.5f);
        goldDisplay.setFontScale(0.5f);
        assertNotNull("Gold display should handle multiple font scale changes", goldDisplay);
    }

    @Test
    public void testSetFontScaleZero() {
        goldDisplay.setFontScale(0f);
        assertNotNull("Gold display should handle zero font scale", goldDisplay);
    }

    @Test
    public void testSetFontScaleNegative() {
        goldDisplay.setFontScale(-1f);
        assertNotNull("Gold display should handle negative font scale", goldDisplay);
    }

    // ===== Dispose Tests =====

    @Test
    public void testDispose() {
        goldDisplay.dispose();
        // Should not throw exception
    }

    @Test
    public void testDisposeMultipleTimes() {
        goldDisplay.dispose();
        goldDisplay.dispose();
        // Should not throw exception even when called multiple times
    }

    @Test
    public void testDisposeWithIcon() {
        gold goldWithIcon = new gold(100, 200, "coin.png");
        goldWithIcon.dispose();
        // Should not throw exception
    }

    // ===== Integration Tests =====

    @Test
    public void testFullGameLoop() {
        for (int i = 0; i < 60; i++) {
            goldDisplay.addGold(1);
            goldDisplay.render(mockBatch);
        }
        
        assertEquals("Gold should be 60 after 60 frames", 60, goldDisplay.getGold());
        verify(mockBatch, atLeastOnce()).begin();
    }

    @Test
    public void testPurchaseSequence() {
        goldDisplay.setGold(1000);
        
        assertTrue("Purchase 1 should succeed", goldDisplay.removeGold(100));
        assertEquals("Gold should be 900", 900, goldDisplay.getGold());
        
        assertTrue("Purchase 2 should succeed", goldDisplay.removeGold(250));
        assertEquals("Gold should be 650", 650, goldDisplay.getGold());
        
        assertFalse("Purchase 3 should fail", goldDisplay.removeGold(700));
        assertEquals("Gold should still be 650", 650, goldDisplay.getGold());
    }

    @Test
    public void testEarningAndSpending() {
        goldDisplay.setGold(0);
        
        goldDisplay.addGold(50);
        goldDisplay.addGold(50);
        assertEquals("Should have 100 gold", 100, goldDisplay.getGold());
        
        goldDisplay.removeGold(30);
        assertEquals("Should have 70 gold", 70, goldDisplay.getGold());
        
        goldDisplay.addGold(80);
        assertEquals("Should have 150 gold", 150, goldDisplay.getGold());
    }

    @Test
    public void testRenderWithChangingGold() {
        for (int i = 0; i <= 100; i += 10) {
            goldDisplay.setGold(i);
            goldDisplay.render(mockBatch);
        }
        
        assertEquals("Final gold should be 100", 100, goldDisplay.getGold());
        verify(mockBatch, atLeastOnce()).begin();
    }

    @Test
    public void testCompleteUILifecycle() {
        // Initialize
        gold ui = new gold(10, 10);
        ui.setGold(0);
        
        // Customize
        ui.setColor(Color.YELLOW);
        ui.setFontScale(1.5f);
        
        // Game loop
        for (int i = 0; i < 100; i++) {
            ui.addGold(5);
            if (ui.getGold() >= 100) {
                ui.removeGold(100);
            }
            ui.render(mockBatch);
        }
        
        // Cleanup
        ui.dispose();
        
        assertTrue("Gold should be positive", ui.getGold() >= 0);
    }

    @Test
    public void testPositionAndRenderSequence() {
        goldDisplay.setPosition(100, 100);
        goldDisplay.setGold(50);
        goldDisplay.render(mockBatch);
        
        goldDisplay.setPosition(200, 200);
        goldDisplay.setGold(100);
        goldDisplay.render(mockBatch);
        
        verify(mockBatch, atLeastOnce()).begin();
    }

    @Test
    public void testLargeGoldValues() {
        goldDisplay.setGold(999999);
        assertEquals("Should handle large gold values", 999999, goldDisplay.getGold());
        
        goldDisplay.addGold(1);
        assertEquals("Should handle adding to large values", 1000000, goldDisplay.getGold());
        
        goldDisplay.render(mockBatch);
        verify(mockBatch, atLeastOnce()).begin();
    }

    @Test
    public void testEdgeCaseGoldOperations() {
        // Start with 0
        assertEquals("Initial gold is 0", 0, goldDisplay.getGold());
        
        // Try to remove when empty
        assertFalse("Cannot remove from empty", goldDisplay.removeGold(1));
        
        // Add then remove exact amount
        goldDisplay.addGold(100);
        assertTrue("Can remove exact amount", goldDisplay.removeGold(100));
        assertEquals("Back to 0", 0, goldDisplay.getGold());
        
        // Negative operations
        goldDisplay.setGold(-50);
        assertEquals("Negative clamped to 0", 0, goldDisplay.getGold());
    }

    @Test
    public void testNonNegativeGoldInvariant() {
        goldDisplay.setGold(100);
        
        // Various operations that could make gold negative
        goldDisplay.update(-50);
        assertTrue("Gold should be non-negative", goldDisplay.getGold() >= 0);
        
        goldDisplay.setGold(-100);
        assertTrue("Gold should be non-negative", goldDisplay.getGold() >= 0);
        
        goldDisplay.addGold(-200);
        assertTrue("Gold should be non-negative", goldDisplay.getGold() >= 0);
    }
}