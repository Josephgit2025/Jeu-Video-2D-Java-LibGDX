package com.ui;

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class healthbarTest {

    private static Application application;
    private healthbar healthBar;
    
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
        
        healthBar = new healthbar(10, 10, 200, 20);
    }

    // ===== Constructor Tests =====

    @Test
    public void testConstructorWithoutIcon() {
        assertNotNull("HealthBar should not be null", healthBar);
        assertEquals("Initial health should be 100", 100, healthBar.getCurrentHealth());
        assertEquals("Initial max health should be 100", 100, healthBar.getMaxHealth());
    }

    @Test
    public void testConstructorWithIcon() {
        healthbar barWithIcon = new healthbar(10, 10, 200, 20, "heart.png");
        assertNotNull("HealthBar with icon should not be null", barWithIcon);
        assertEquals("Initial health should be 100", 100, barWithIcon.getCurrentHealth());
        assertEquals("Initial max health should be 100", 100, barWithIcon.getMaxHealth());
    }

    @Test
    public void testConstructorWithDifferentDimensions() {
        healthbar small = new healthbar(0, 0, 100, 10);
        healthbar large = new healthbar(0, 0, 500, 50);
        
        assertNotNull("Small health bar should not be null", small);
        assertNotNull("Large health bar should not be null", large);
    }

    @Test
    public void testConstructorWithInvalidIconPath() {
        healthbar barWithInvalidIcon = new healthbar(10, 10, 200, 20, "invalid/path/heart.png");
        assertNotNull("HealthBar should handle invalid icon path", barWithInvalidIcon);
        assertEquals("Health should still initialize", 100, barWithInvalidIcon.getCurrentHealth());
    }

    @Test
    public void testConstructorWithZeroDimensions() {
        healthbar zeroBar = new healthbar(0, 0, 0, 0);
        assertNotNull("HealthBar with zero dimensions should not crash", zeroBar);
    }

    @Test
    public void testConstructorWithNegativePosition() {
        healthbar negativePos = new healthbar(-50, -100, 200, 20);
        assertNotNull("HealthBar with negative position should not crash", negativePos);
    }

    // ===== Health Value Tests =====

    @Test
    public void testGetCurrentHealthInitial() {
        assertEquals("Initial current health should be 100", 100, healthBar.getCurrentHealth());
    }

    @Test
    public void testGetMaxHealthInitial() {
        assertEquals("Initial max health should be 100", 100, healthBar.getMaxHealth());
    }

    @Test
    public void testUpdateHealth() {
        healthBar.update(50, 100);
        assertEquals("Current health should be 50", 50, healthBar.getCurrentHealth());
        assertEquals("Max health should be 100", 100, healthBar.getMaxHealth());
    }

    @Test
    public void testUpdateHealthMultipleTimes() {
        healthBar.update(75, 100);
        assertEquals("Health should be 75", 75, healthBar.getCurrentHealth());
        
        healthBar.update(30, 100);
        assertEquals("Health should be updated to 30", 30, healthBar.getCurrentHealth());
        
        healthBar.update(90, 100);
        assertEquals("Health should be updated to 90", 90, healthBar.getCurrentHealth());
    }

    @Test
    public void testUpdateWithNegativeCurrentHealth() {
        healthBar.update(-50, 100);
        assertEquals("Negative health should be clamped to 0", 0, healthBar.getCurrentHealth());
        assertEquals("Max health should remain 100", 100, healthBar.getMaxHealth());
    }

    @Test
    public void testUpdateWithNegativeMaxHealth() {
        healthBar.update(50, -100);
        assertEquals("Current health should be 50", 50, healthBar.getCurrentHealth());
        assertEquals("Negative max health should be clamped to 1", 1, healthBar.getMaxHealth());
    }

    @Test
    public void testUpdateWithZeroMaxHealth() {
        healthBar.update(50, 0);
        assertEquals("Zero max health should be clamped to 1", 1, healthBar.getMaxHealth());
    }

    @Test
    public void testUpdateWithHealthGreaterThanMax() {
        healthBar.update(150, 100);
        assertEquals("Health can be greater than max", 150, healthBar.getCurrentHealth());
        assertEquals("Max health should be 100", 100, healthBar.getMaxHealth());
    }

    @Test
    public void testUpdateToZeroHealth() {
        healthBar.update(0, 100);
        assertEquals("Health should be 0", 0, healthBar.getCurrentHealth());
    }

    @Test
    public void testUpdateToFullHealth() {
        healthBar.update(50, 100);
        healthBar.update(100, 100);
        assertEquals("Health should be full", 100, healthBar.getCurrentHealth());
    }

    @Test
    public void testUpdateMaxHealthChange() {
        healthBar.update(50, 100);
        healthBar.update(50, 200);
        assertEquals("Current health should remain 50", 50, healthBar.getCurrentHealth());
        assertEquals("Max health should be 200", 200, healthBar.getMaxHealth());
    }

    @Test
    public void testUpdateWithLargeValues() {
        healthBar.update(10000, 20000);
        assertEquals("Should handle large health values", 10000, healthBar.getCurrentHealth());
        assertEquals("Should handle large max health values", 20000, healthBar.getMaxHealth());
    }

    @Test
    public void testHealthPercentageCalculations() {
        // Full health (100%)
        healthBar.update(100, 100);
        assertEquals("Full health", 100, healthBar.getCurrentHealth());
        
        // Half health (50%)
        healthBar.update(50, 100);
        assertEquals("Half health", 50, healthBar.getCurrentHealth());
        
        // Quarter health (25%)
        healthBar.update(25, 100);
        assertEquals("Quarter health", 25, healthBar.getCurrentHealth());
        
        // Near death (10%)
        healthBar.update(10, 100);
        assertEquals("Low health", 10, healthBar.getCurrentHealth());
    }

    // ===== Position and Size Tests =====

    @Test
    public void testSetPosition() {
        healthBar.setPosition(100, 200);
        assertNotNull("HealthBar should still be valid", healthBar);
    }

    @Test
    public void testSetPositionMultipleTimes() {
        healthBar.setPosition(50, 50);
        healthBar.setPosition(100, 100);
        healthBar.setPosition(200, 300);
        assertNotNull("HealthBar should handle position changes", healthBar);
    }

    @Test
    public void testSetPositionNegative() {
        healthBar.setPosition(-100, -200);
        assertNotNull("HealthBar should handle negative positions", healthBar);
    }

    @Test
    public void testSetPositionZero() {
        healthBar.setPosition(0, 0);
        assertNotNull("HealthBar should handle zero position", healthBar);
    }

    @Test
    public void testSetSize() {
        healthBar.setSize(300, 30);
        assertNotNull("HealthBar should still be valid", healthBar);
    }

    @Test
    public void testSetSizeMultipleTimes() {
        healthBar.setSize(100, 10);
        healthBar.setSize(250, 25);
        healthBar.setSize(400, 40);
        assertNotNull("HealthBar should handle size changes", healthBar);
    }

    @Test
    public void testSetSizeZero() {
        healthBar.setSize(0, 0);
        assertNotNull("HealthBar should handle zero size", healthBar);
    }

    @Test
    public void testSetSizeNegative() {
        healthBar.setSize(-100, -20);
        assertNotNull("HealthBar should handle negative size", healthBar);
    }

    @Test
    public void testSetSizeVeryLarge() {
        healthBar.setSize(10000, 1000);
        assertNotNull("HealthBar should handle very large sizes", healthBar);
    }

    // ===== Render Tests =====

    @Test
    public void testRenderWithShapeRenderer() {
        healthBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
        verify(mockShapeRenderer, atLeastOnce()).end();
    }

    @Test
    public void testRenderWithFullHealth() {
        healthBar.update(100, 100);
        healthBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
        verify(mockShapeRenderer, atLeastOnce()).end();
    }

    @Test
    public void testRenderWithHalfHealth() {
        healthBar.update(50, 100);
        healthBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
        verify(mockShapeRenderer, atLeastOnce()).end();
    }

    @Test
    public void testRenderWithLowHealth() {
        healthBar.update(10, 100);
        healthBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
        verify(mockShapeRenderer, atLeastOnce()).end();
    }

    @Test
    public void testRenderWithZeroHealth() {
        healthBar.update(0, 100);
        healthBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
        verify(mockShapeRenderer, atLeastOnce()).end();
    }

    @Test
    public void testRenderMultipleTimes() {
        healthBar.render(mockShapeRenderer);
        healthBar.render(mockShapeRenderer);
        healthBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
        verify(mockShapeRenderer, atLeastOnce()).end();
    }

    @Test
    public void testRenderWithBatchAndShapeRenderer() {
        healthBar.render(mockShapeRenderer, mockBatch);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
        verify(mockShapeRenderer, atLeastOnce()).end();
    }

    @Test
    public void testRenderWithBatchAndIcon() {
        healthbar barWithIcon = new healthbar(10, 10, 200, 20, "heart.png");
        barWithIcon.render(mockShapeRenderer, mockBatch);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
        verify(mockShapeRenderer, atLeastOnce()).end();
    }

    @Test
    public void testRenderAfterHealthUpdate() {
        healthBar.update(75, 100);
        healthBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
    }

    @Test
    public void testRenderAfterPositionChange() {
        healthBar.setPosition(300, 400);
        healthBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
    }

    @Test
    public void testRenderAfterSizeChange() {
        healthBar.setSize(400, 40);
        healthBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
    }

    // ===== Health Color Tests (Implicit through rendering) =====

    @Test
    public void testRenderGreenHealthColor() {
        // High health > 50% should render green
        healthBar.update(80, 100);
        healthBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
    }

    @Test
    public void testRenderOrangeHealthColor() {
        // Medium health 25% < health <= 50% should render orange
        healthBar.update(40, 100);
        healthBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
    }

    @Test
    public void testRenderRedHealthColor() {
        // Low health <= 25% should render red
        healthBar.update(20, 100);
        healthBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
    }

    @Test
    public void testRenderAtExactThresholds() {
        // Test at 50% threshold
        healthBar.update(50, 100);
        healthBar.render(mockShapeRenderer);
        
        // Test at 25% threshold
        healthBar.update(25, 100);
        healthBar.render(mockShapeRenderer);
        
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
    }

    // ===== Dispose Tests =====

    @Test
    public void testDispose() {
        healthBar.dispose();
        // Should not throw exception
    }

    @Test
    public void testDisposeMultipleTimes() {
        healthBar.dispose();
        healthBar.dispose();
        // Should not throw exception even when called multiple times
    }

    @Test
    public void testDisposeWithIcon() {
        healthbar barWithIcon = new healthbar(10, 10, 200, 20, "heart.png");
        barWithIcon.dispose();
        // Should not throw exception
    }

    @Test
    public void testDisposeAfterRender() {
        healthBar.render(mockShapeRenderer);
        healthBar.dispose();
        // Should not throw exception
    }

    // ===== Integration Tests =====

    @Test
    public void testDamageSequence() {
        healthBar.update(100, 100);
        assertEquals("Start with full health", 100, healthBar.getCurrentHealth());
        
        healthBar.update(80, 100);
        assertEquals("After damage, health is 80", 80, healthBar.getCurrentHealth());
        
        healthBar.update(50, 100);
        assertEquals("After more damage, health is 50", 50, healthBar.getCurrentHealth());
        
        healthBar.update(20, 100);
        assertEquals("Critical health at 20", 20, healthBar.getCurrentHealth());
        
        healthBar.update(0, 100);
        assertEquals("Dead at 0 health", 0, healthBar.getCurrentHealth());
    }

    @Test
    public void testHealingSequence() {
        healthBar.update(20, 100);
        assertEquals("Start with low health", 20, healthBar.getCurrentHealth());
        
        healthBar.update(40, 100);
        assertEquals("After healing", 40, healthBar.getCurrentHealth());
        
        healthBar.update(70, 100);
        assertEquals("After more healing", 70, healthBar.getCurrentHealth());
        
        healthBar.update(100, 100);
        assertEquals("Back to full health", 100, healthBar.getCurrentHealth());
    }

    @Test
    public void testCombatLoop() {
        for (int i = 0; i <= 10; i++) {
            healthBar.update(100 - i * 10, 100);
            healthBar.render(mockShapeRenderer);
        }
        
        assertEquals("Health should be 0 after 10 hits", 0, healthBar.getCurrentHealth());
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
    }

    @Test
    public void testLevelUpScenario() {
        // Character levels up and max health increases
        healthBar.update(50, 100);
        assertEquals("Health at 50/100", 50, healthBar.getCurrentHealth());
        
        healthBar.update(50, 150);
        assertEquals("Max health increased to 150", 150, healthBar.getMaxHealth());
        assertEquals("Current health stays at 50", 50, healthBar.getCurrentHealth());
        
        healthBar.update(150, 150);
        assertEquals("Healed to full new max", 150, healthBar.getCurrentHealth());
    }

    @Test
    public void testBossHealthBar() {
        // Large boss with high HP
        healthbar bossBar = new healthbar(0, 0, 800, 40);
        bossBar.update(50000, 50000);
        
        assertEquals("Boss at full health", 50000, bossBar.getCurrentHealth());
        
        bossBar.update(25000, 50000);
        assertEquals("Boss at half health", 25000, bossBar.getCurrentHealth());
        
        bossBar.render(mockShapeRenderer);
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
        
        bossBar.dispose();
    }

    @Test
    public void testRegenerationLoop() {
        healthBar.update(50, 100);
        
        // Simulate regeneration over time
        for (int i = 0; i <= 50; i++) {
            int newHealth = Math.min(50 + i, 100);
            healthBar.update(newHealth, 100);
        }
        
        assertEquals("Should regenerate to full", 100, healthBar.getCurrentHealth());
    }

    @Test
    public void testPoisonDamageLoop() {
        healthBar.update(100, 100);
        
        // Simulate poison damage over time
        for (int i = 0; i <= 20; i++) {
            int newHealth = Math.max(100 - i * 5, 0);
            healthBar.update(newHealth, 100);
            healthBar.render(mockShapeRenderer);
        }
        
        assertEquals("Poison should kill player", 0, healthBar.getCurrentHealth());
    }

    @Test
    public void testCompleteUILifecycle() {
        // Initialize
        healthbar ui = new healthbar(10, 580, 300, 25, "heart.png");
        ui.update(100, 100);
        
        // Combat simulation
        for (int i = 0; i < 5; i++) {
            ui.update(100 - i * 15, 100);
            ui.render(mockShapeRenderer, mockBatch);
        }
        
        // Reposition for different screen
        ui.setPosition(20, 560);
        ui.setSize(400, 30);
        
        // Continue combat
        for (int i = 5; i < 10; i++) {
            ui.update(100 - i * 15, 100);
            ui.render(mockShapeRenderer, mockBatch);
        }
        
        // Cleanup
        ui.dispose();
        
        assertTrue("Health should be non-negative", ui.getCurrentHealth() >= 0);
    }

    @Test
    public void testMultipleHealthBars() {
        healthbar player = new healthbar(10, 580, 200, 20);
        healthbar enemy1 = new healthbar(300, 400, 150, 15);
        healthbar enemy2 = new healthbar(500, 200, 150, 15);
        
        player.update(100, 100);
        enemy1.update(50, 50);
        enemy2.update(30, 30);
        
        player.render(mockShapeRenderer);
        enemy1.render(mockShapeRenderer);
        enemy2.render(mockShapeRenderer);
        
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
        
        player.dispose();
        enemy1.dispose();
        enemy2.dispose();
    }

    @Test
    public void testEdgeCaseHealthPercentages() {
        // Test at 51% (should be green)
        healthBar.update(51, 100);
        healthBar.render(mockShapeRenderer);
        
        // Test at 50% (should be orange)
        healthBar.update(50, 100);
        healthBar.render(mockShapeRenderer);
        
        // Test at 26% (should be orange)
        healthBar.update(26, 100);
        healthBar.render(mockShapeRenderer);
        
        // Test at 25% (should be red)
        healthBar.update(25, 100);
        healthBar.render(mockShapeRenderer);
        
        // Test at 1% (should be red)
        healthBar.update(1, 100);
        healthBar.render(mockShapeRenderer);
        
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
    }

    @Test
    public void testNonNegativeHealthInvariant() {
        // Various operations that should keep health non-negative
        healthBar.update(-100, 100);
        assertTrue("Health should be non-negative", healthBar.getCurrentHealth() >= 0);
        
        healthBar.update(-50, -50);
        assertTrue("Health should be non-negative", healthBar.getCurrentHealth() >= 0);
        assertTrue("Max health should be positive", healthBar.getMaxHealth() > 0);
    }

    @Test
    public void testRenderAtDifferentHealthStates() {
        int[] healthStates = {100, 90, 75, 60, 50, 40, 30, 25, 20, 10, 5, 1, 0};
        
        for (int health : healthStates) {
            healthBar.update(health, 100);
            healthBar.render(mockShapeRenderer);
        }
        
        verify(mockShapeRenderer, atLeastOnce()).begin(any());
    }

    @Test
    public void testMaxHealthAlwaysPositive() {
        healthBar.update(50, 0);
        assertTrue("Max health should be at least 1", healthBar.getMaxHealth() >= 1);
        
        healthBar.update(50, -100);
        assertTrue("Max health should be at least 1", healthBar.getMaxHealth() >= 1);
        
        healthBar.update(50, -1);
        assertTrue("Max health should be at least 1", healthBar.getMaxHealth() >= 1);
    }
}