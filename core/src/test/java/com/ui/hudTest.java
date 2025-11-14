package com.ui;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class hudTest {

    private static Application application;
    private hud gameHud;
    
    @Mock
    private SpriteBatch mockBatch;
    
    @Mock
    private ShapeRenderer mockShapeRenderer;
    
    @Mock
    private BitmapFont mockFont;
    
    @Mock
    private healthbar mockHealthBar;
    
    @Mock
    private gold mockGold;
    
    private GL20 mockGL;
    private Graphics mockGraphics;
    
    // Track gold state
    private AtomicInteger goldAmount;

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
        
        mockGL = mock(GL20.class);
        mockGraphics = mock(Graphics.class);
        
        Gdx.gl = mockGL;
        Gdx.gl20 = mockGL;
        Gdx.graphics = mockGraphics;
        
        when(mockGraphics.getWidth()).thenReturn(800);
        when(mockGraphics.getHeight()).thenReturn(600);
        
        // Setup font mock
        BitmapFont.BitmapFontData mockFontData = mock(BitmapFont.BitmapFontData.class);
        when(mockFont.getData()).thenReturn(mockFontData);
        
        // Initialize gold amount tracker
        goldAmount = new AtomicInteger(0);
        
        // Setup gold mock with state tracking
        when(mockGold.getGold()).thenAnswer(invocation -> goldAmount.get());
        
        doAnswer(invocation -> {
            int amount = invocation.getArgument(0);
            goldAmount.addAndGet(amount);
            return null;
        }).when(mockGold).addGold(org.mockito.ArgumentMatchers.anyInt());
        
        doAnswer(invocation -> {
            int amount = invocation.getArgument(0);
            goldAmount.set(Math.max(0, amount));
            return null;
        }).when(mockGold).setGold(org.mockito.ArgumentMatchers.anyInt());
        
        doAnswer(invocation -> {
            int amount = invocation.getArgument(0);
            goldAmount.set(Math.max(0, amount));
            return null;
        }).when(mockGold).update(org.mockito.ArgumentMatchers.anyInt());
        
        when(mockGold.removeGold(org.mockito.ArgumentMatchers.anyInt())).thenAnswer(invocation -> {
            int amount = invocation.getArgument(0);
            int current = goldAmount.get();
            if (current >= amount) {
                goldAmount.set(current - amount);
                return true;
            }
            return false;
        });
        
        // Create HUD with mocked dependencies
        gameHud = new hud(mockBatch, mockShapeRenderer, mockFont, mockHealthBar, mockGold);
    }

    // ===== Constructor Tests =====

    @Test
    public void testConstructor() {
        assertNotNull("HUD should not be null", gameHud);
    }

    @Test
    public void testConstructorInitializesGold() {
        assertEquals("Initial gold should be 0", 0, gameHud.getGold());
    }

    @Test
    public void testMultipleHudInstances() {
        // Create multiple HUD instances with mocks to avoid shader compilation
        SpriteBatch mockBatch1 = mock(SpriteBatch.class);
        ShapeRenderer mockShapeRenderer1 = mock(ShapeRenderer.class);
        BitmapFont mockFont1 = mock(BitmapFont.class);
        BitmapFont.BitmapFontData mockFontData1 = mock(BitmapFont.BitmapFontData.class);
        when(mockFont1.getData()).thenReturn(mockFontData1);
        healthbar mockHealthBar1 = mock(healthbar.class);
        gold mockGold1 = mock(gold.class);
        when(mockGold1.getGold()).thenReturn(0);
        
        SpriteBatch mockBatch2 = mock(SpriteBatch.class);
        ShapeRenderer mockShapeRenderer2 = mock(ShapeRenderer.class);
        BitmapFont mockFont2 = mock(BitmapFont.class);
        BitmapFont.BitmapFontData mockFontData2 = mock(BitmapFont.BitmapFontData.class);
        when(mockFont2.getData()).thenReturn(mockFontData2);
        healthbar mockHealthBar2 = mock(healthbar.class);
        gold mockGold2 = mock(gold.class);
        when(mockGold2.getGold()).thenReturn(0);
        
        hud hud1 = new hud(mockBatch1, mockShapeRenderer1, mockFont1, mockHealthBar1, mockGold1);
        hud hud2 = new hud(mockBatch2, mockShapeRenderer2, mockFont2, mockHealthBar2, mockGold2);
        
        assertNotNull("First HUD should not be null", hud1);
        assertNotNull("Second HUD should not be null", hud2);
        
        hud1.dispose();
        hud2.dispose();
    }

    // ===== Update Tests =====

    @Test
    public void testUpdateWithValidValues() {
        gameHud.update(80, 100, 500);
        assertEquals("Gold should be 500", 500, gameHud.getGold());
    }

    @Test
    public void testUpdateMultipleTimes() {
        gameHud.update(100, 100, 100);
        assertEquals("Gold should be 100", 100, gameHud.getGold());
        
        gameHud.update(75, 100, 200);
        assertEquals("Gold should be 200", 200, gameHud.getGold());
        
        gameHud.update(50, 100, 300);
        assertEquals("Gold should be 300", 300, gameHud.getGold());
    }

    @Test
    public void testUpdateWithZeroHealth() {
        gameHud.update(0, 100, 250);
        assertEquals("Gold should be 250 even with zero health", 250, gameHud.getGold());
    }

    @Test
    public void testUpdateWithNegativeValues() {
        gameHud.update(-50, 100, -100);
        // Gold should be clamped to 0 (from gold component)
        assertTrue("Gold should be non-negative", gameHud.getGold() >= 0);
    }

    @Test
    public void testUpdateWithMaxHealth() {
        gameHud.update(100, 100, 1000);
        assertEquals("Gold should be 1000", 1000, gameHud.getGold());
    }

    @Test
    public void testUpdateWithLargeValues() {
        gameHud.update(50000, 50000, 999999);
        assertEquals("Should handle large gold values", 999999, gameHud.getGold());
    }

    @Test
    public void testUpdateHealthOnly() {
        gameHud.setGold(500);
        gameHud.update(80, 100, 500);
        assertEquals("Gold should remain 500", 500, gameHud.getGold());
    }

    @Test
    public void testUpdateGoldOnly() {
        gameHud.update(100, 100, 250);
        gameHud.update(100, 100, 300);
        assertEquals("Gold should be updated to 300", 300, gameHud.getGold());
    }

    // ===== Gold Management Tests =====

    @Test
    public void testGetGoldInitial() {
        assertEquals("Initial gold should be 0", 0, gameHud.getGold());
    }

    @Test
    public void testSetGold() {
        gameHud.setGold(500);
        assertEquals("Gold should be 500", 500, gameHud.getGold());
    }

    @Test
    public void testSetGoldMultipleTimes() {
        gameHud.setGold(100);
        gameHud.setGold(200);
        gameHud.setGold(300);
        assertEquals("Gold should be 300", 300, gameHud.getGold());
    }

    @Test
    public void testSetGoldToZero() {
        gameHud.setGold(100);
        gameHud.setGold(0);
        assertEquals("Gold should be 0", 0, gameHud.getGold());
    }

    @Test
    public void testSetGoldNegative() {
        gameHud.setGold(-100);
        assertEquals("Negative gold should be clamped to 0", 0, gameHud.getGold());
    }

    @Test
    public void testAddGold() {
        gameHud.setGold(100);
        gameHud.addGold(50);
        assertEquals("Gold should be 150", 150, gameHud.getGold());
    }

    @Test
    public void testAddGoldMultipleTimes() {
        gameHud.addGold(10);
        gameHud.addGold(20);
        gameHud.addGold(30);
        assertEquals("Gold should be 60", 60, gameHud.getGold());
    }

    @Test
    public void testAddGoldZero() {
        gameHud.setGold(100);
        gameHud.addGold(0);
        assertEquals("Gold should remain 100", 100, gameHud.getGold());
    }

    @Test
    public void testAddNegativeGold() {
        gameHud.setGold(100);
        gameHud.addGold(-30);
        assertEquals("Gold should decrease to 70", 70, gameHud.getGold());
    }

    @Test
    public void testAddLargeGoldAmount() {
        gameHud.addGold(1000000);
        assertEquals("Should handle large gold amounts", 1000000, gameHud.getGold());
    }

    @Test
    public void testRemoveGoldSuccess() {
        gameHud.setGold(100);
        boolean result = gameHud.removeGold(50);
        
        assertTrue("Remove should succeed", result);
        assertEquals("Gold should be 50", 50, gameHud.getGold());
    }

    @Test
    public void testRemoveGoldFailure() {
        gameHud.setGold(30);
        boolean result = gameHud.removeGold(50);
        
        assertFalse("Remove should fail", result);
        assertEquals("Gold should remain 30", 30, gameHud.getGold());
    }

    @Test
    public void testRemoveGoldExactAmount() {
        gameHud.setGold(100);
        boolean result = gameHud.removeGold(100);
        
        assertTrue("Remove exact amount should succeed", result);
        assertEquals("Gold should be 0", 0, gameHud.getGold());
    }

    @Test
    public void testRemoveGoldMultipleTimes() {
        gameHud.setGold(100);
        
        assertTrue("First removal", gameHud.removeGold(20));
        assertEquals("Gold should be 80", 80, gameHud.getGold());
        
        assertTrue("Second removal", gameHud.removeGold(30));
        assertEquals("Gold should be 50", 50, gameHud.getGold());
        
        assertFalse("Third removal should fail", gameHud.removeGold(60));
        assertEquals("Gold should still be 50", 50, gameHud.getGold());
    }

    @Test
    public void testRemoveZeroGold() {
        gameHud.setGold(100);
        boolean result = gameHud.removeGold(0);
        
        assertTrue("Removing 0 gold should succeed", result);
        assertEquals("Gold should remain 100", 100, gameHud.getGold());
    }

    @Test
    public void testRemoveGoldFromEmpty() {
        gameHud.setGold(0);
        boolean result = gameHud.removeGold(50);
        
        assertFalse("Cannot remove from empty", result);
        assertEquals("Gold should remain 0", 0, gameHud.getGold());
    }

    // ===== Render Tests =====

    @Test
    public void testRender() {
        gameHud.render();
    }

    @Test
    public void testRenderMultipleTimes() {
        gameHud.render();
        gameHud.render();
        gameHud.render();
    }

    @Test
    public void testRenderAfterUpdate() {
        gameHud.update(75, 100, 500);
        gameHud.render();
    }

    @Test
    public void testRenderWithZeroHealth() {
        gameHud.update(0, 100, 100);
        gameHud.render();
    }

    @Test
    public void testRenderWithZeroGold() {
        gameHud.update(100, 100, 0);
        gameHud.render();
    }

    @Test
    public void testRenderWithMaxValues() {
        gameHud.update(100, 100, 999999);
        gameHud.render();
    }

    @Test
    public void testRenderAfterResize() {
        gameHud.resize(1920, 1080);
        gameHud.render();
    }

    // ===== Resize Tests =====

    @Test
    public void testResize() {
        gameHud.resize(1024, 768);
        assertNotNull("HUD should still be valid after resize", gameHud);
    }

    @Test
    public void testResizeMultipleTimes() {
        gameHud.resize(800, 600);
        gameHud.resize(1920, 1080);
        gameHud.resize(1280, 720);
        assertNotNull("HUD should handle multiple resizes", gameHud);
    }

    @Test
    public void testResizeToSmallWindow() {
        gameHud.resize(320, 240);
        assertNotNull("HUD should handle small window", gameHud);
    }

    @Test
    public void testResizeToLargeWindow() {
        gameHud.resize(3840, 2160);
        assertNotNull("HUD should handle large window", gameHud);
    }

    @Test
    public void testResizePreservesGold() {
        gameHud.setGold(500);
        gameHud.resize(1920, 1080);
        assertEquals("Gold should be preserved after resize", 500, gameHud.getGold());
    }

    @Test
    public void testResizeAndRender() {
        gameHud.resize(1280, 720);
        gameHud.render();
    }

    @Test
    public void testResizeZeroDimensions() {
        gameHud.resize(0, 0);
        assertNotNull("HUD should handle zero dimensions", gameHud);
    }

    @Test
    public void testResizeNegativeDimensions() {
        gameHud.resize(-100, -100);
        assertNotNull("HUD should handle negative dimensions", gameHud);
    }

    // ===== Dispose Tests =====

    @Test
    public void testDispose() {
        gameHud.dispose();
    }

    @Test
    public void testDisposeMultipleTimes() {
        gameHud.dispose();
        gameHud.dispose();
    }

    @Test
    public void testDisposeAfterRender() {
        gameHud.render();
        gameHud.dispose();
    }

    @Test
    public void testDisposeAfterUpdate() {
        gameHud.update(100, 100, 500);
        gameHud.dispose();
    }

    // ===== Integration Tests =====

    @Test
    public void testCompleteGameLoop() {
        gameHud.update(100, 100, 0);
        
        for (int i = 0; i < 60; i++) {
            gameHud.addGold(10);
            gameHud.update(100 - i, 100, gameHud.getGold());
            gameHud.render();
        }
        
        assertEquals("Gold should accumulate", 600, gameHud.getGold());
    }

    @Test
    public void testCombatScenario() {
        gameHud.update(100, 100, 500);
        gameHud.update(80, 100, 500);
        gameHud.render();
        
        assertTrue("Should have enough gold for potion", gameHud.removeGold(50));
        gameHud.update(100, 100, gameHud.getGold());
        gameHud.render();
        
        assertEquals("Gold should be 450", 450, gameHud.getGold());
    }

    @Test
    public void testShoppingScenario() {
        gameHud.setGold(1000);
        
        assertTrue("Purchase 1", gameHud.removeGold(200));
        assertEquals("800 gold left", 800, gameHud.getGold());
        
        assertTrue("Purchase 2", gameHud.removeGold(350));
        assertEquals("450 gold left", 450, gameHud.getGold());
        
        assertFalse("Cannot afford", gameHud.removeGold(500));
        assertEquals("Still 450 gold", 450, gameHud.getGold());
    }

    @Test
    public void testLootCollection() {
        gameHud.setGold(0);
        
        gameHud.addGold(50);
        gameHud.addGold(75);
        gameHud.addGold(100);
        
        assertEquals("Should have 225 gold", 225, gameHud.getGold());
        gameHud.render();
    }

    @Test
    public void testHealthRegenerationWithGold() {
        gameHud.update(50, 100, 300);
        
        for (int hp = 50; hp <= 100; hp += 10) {
            gameHud.update(hp, 100, gameHud.getGold());
            gameHud.render();
        }
        
        assertEquals("Gold should be unchanged", 300, gameHud.getGold());
    }

    @Test
    public void testDeathScenario() {
        gameHud.setGold(1000);
        gameHud.update(100, 100, 1000);
        gameHud.update(0, 100, gameHud.getGold());
        gameHud.render();
        
        gameHud.removeGold(500);
        assertEquals("Lost gold on death", 500, gameHud.getGold());
        
        gameHud.update(100, 100, gameHud.getGold());
        gameHud.render();
    }

    @Test
    public void testLevelUpScenario() {
        gameHud.update(50, 100, 200);
        gameHud.update(150, 150, 200);
        gameHud.render();
        
        gameHud.addGold(500);
        assertEquals("Should have 700 gold", 700, gameHud.getGold());
    }

    @Test
    public void testBossDefeatReward() {
        gameHud.setGold(100);
        gameHud.update(25, 100, 100);
        
        gameHud.addGold(5000);
        assertEquals("Should have boss reward", 5100, gameHud.getGold());
        
        gameHud.render();
    }

    @Test
    public void testMultipleResizesInGameLoop() {
        for (int i = 0; i < 5; i++) {
            gameHud.resize(800 + i * 100, 600 + i * 50);
            gameHud.update(100, 100, i * 50);
            gameHud.render();
        }
        
        assertTrue("Gold should be accumulated", gameHud.getGold() >= 0);
    }

    @Test
    public void testFullGameSession() {
        gameHud.update(100, 100, 0);
        gameHud.render();
        
        for (int i = 0; i < 10; i++) {
            gameHud.addGold(25);
            gameHud.render();
        }
        assertEquals("Should have 250 gold", 250, gameHud.getGold());
        
        gameHud.update(60, 100, gameHud.getGold());
        gameHud.removeGold(100);
        assertEquals("Should have 150 gold", 150, gameHud.getGold());
        
        gameHud.update(100, 100, gameHud.getGold());
        gameHud.update(10, 100, gameHud.getGold());
        gameHud.addGold(1000);
        assertEquals("Should have 1150 gold", 1150, gameHud.getGold());
        
        gameHud.render();
        gameHud.dispose();
    }

    @Test
    public void testPersistentHudUpdates() {
        for (int frame = 0; frame < 1000; frame++) {
            int health = 50 + (frame % 51);
            int gold = frame * 2;
            
            gameHud.update(health, 100, gold);
            
            if (frame % 100 == 0) {
                gameHud.render();
            }
        }
        
        assertEquals("Final gold should be correct", 1998, gameHud.getGold());
    }

    @Test
    public void testWindowResizeDuringGameplay() {
        gameHud.update(100, 100, 500);
        gameHud.render();
        
        gameHud.resize(1920, 1080);
        gameHud.render();
        
        gameHud.update(75, 100, 500);
        gameHud.addGold(100);
        gameHud.render();
        
        assertEquals("Gold should be updated", 600, gameHud.getGold());
    }

    @Test
    public void testRapidGoldChanges() {
        gameHud.setGold(1000);
        
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                gameHud.addGold(10);
            } else {
                gameHud.removeGold(5);
            }
        }
        
        assertEquals("Net gold change should be correct", 1250, gameHud.getGold());
    }

    @Test
    public void testEdgeCaseGoldOperations() {
        assertEquals("Initial gold is 0", 0, gameHud.getGold());
        
        assertFalse("Cannot remove from empty", gameHud.removeGold(1));
        
        gameHud.addGold(100);
        assertTrue("Can remove exact amount", gameHud.removeGold(100));
        assertEquals("Back to 0", 0, gameHud.getGold());
    }

    @Test
    public void testCompleteUILifecycle() {
        // Create HUD with mocks
        SpriteBatch mockBatchUI = mock(SpriteBatch.class);
        ShapeRenderer mockShapeRendererUI = mock(ShapeRenderer.class);
        BitmapFont mockFontUI = mock(BitmapFont.class);
        BitmapFont.BitmapFontData mockFontDataUI = mock(BitmapFont.BitmapFontData.class);
        when(mockFontUI.getData()).thenReturn(mockFontDataUI);
        healthbar mockHealthBarUI = mock(healthbar.class);
        gold mockGoldUI = mock(gold.class);
        
        // Setup gold tracking for this instance
        AtomicInteger uiGoldAmount = new AtomicInteger(0);
        when(mockGoldUI.getGold()).thenAnswer(inv -> uiGoldAmount.get());
        doAnswer(inv -> {
            int amount = inv.getArgument(0);
            uiGoldAmount.addAndGet(amount);
            return null;
        }).when(mockGoldUI).addGold(org.mockito.ArgumentMatchers.anyInt());
        doAnswer(inv -> {
            int amount = inv.getArgument(0);
            uiGoldAmount.set(Math.max(0, amount));
            return null;
        }).when(mockGoldUI).setGold(org.mockito.ArgumentMatchers.anyInt());
        doAnswer(inv -> {
            int amount = inv.getArgument(0);
            uiGoldAmount.set(Math.max(0, amount));
            return null;
        }).when(mockGoldUI).update(org.mockito.ArgumentMatchers.anyInt());
        when(mockGoldUI.removeGold(org.mockito.ArgumentMatchers.anyInt())).thenAnswer(inv -> {
            int amount = inv.getArgument(0);
            int current = uiGoldAmount.get();
            if (current >= amount) {
                uiGoldAmount.set(current - amount);
                return true;
            }
            return false;
        });
        
        hud ui = new hud(mockBatchUI, mockShapeRendererUI, mockFontUI, mockHealthBarUI, mockGoldUI);
        ui.update(100, 100, 0);
        
        for (int i = 0; i < 50; i++) {
            ui.addGold(20);
            ui.update(100 - i, 100, ui.getGold());
            
            if (ui.getGold() >= 100) {
                ui.removeGold(100);
            }
            
            ui.render();
        }
        
        ui.resize(1280, 720);
        
        for (int i = 50; i < 100; i++) {
            ui.addGold(10);
            ui.render();
        }
        
        ui.dispose();
        
        assertTrue("Gold should be positive", ui.getGold() >= 0);
    }

    @Test
    public void testNonNegativeGoldInvariant() {
        gameHud.setGold(-100);
        assertTrue("Gold should be non-negative", gameHud.getGold() >= 0);
        
        gameHud.removeGold(500);
        assertTrue("Gold should be non-negative", gameHud.getGold() >= 0);
        
        gameHud.update(-50, 100, -200);
        assertTrue("Gold should be non-negative", gameHud.getGold() >= 0);
    }
}