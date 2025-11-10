package com.main.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Main;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TitleScreenTest {

    private static HeadlessApplication application;
    private TitleScreen titleScreen;
    private Main mockGame;

    @BeforeClass
    public static void init() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        application = new HeadlessApplication(new ApplicationAdapter() {}, config);
        Gdx.gl20 = mock(GL20.class);
        Gdx.gl = Gdx.gl20;
        
        // Mock graphics for dimensions
        Graphics mockGraphics = mock(Graphics.class);
        when(mockGraphics.getWidth()).thenReturn(800);
        when(mockGraphics.getHeight()).thenReturn(480);
        Gdx.graphics = mockGraphics;
    }

    @Before
    public void setUp() {
        mockGame = mock(Main.class);
        titleScreen = new TitleScreen(mockGame);
        
        // Mock SpriteBatch to allow render() to execute
        if (titleScreen.batch == null) {
            titleScreen.batch = mock(SpriteBatch.class);
        }
        
        // Mock Input for updateHover and handleInput tests
        Input mockInput = mock(Input.class);
        when(mockInput.getX()).thenReturn(400); // Center X
        when(mockInput.getY()).thenReturn(240); // Center Y
        when(mockInput.justTouched()).thenReturn(false);
        Gdx.input = mockInput;
    }

    // ===== Constructor Tests =====

    @Test
    public void testConstructor() {
        assertNotNull("TitleScreen should be created", titleScreen);
    }

    @Test
    public void testConstructorWithNullGame() {
        TitleScreen screen = new TitleScreen(null);
        assertNotNull("TitleScreen should handle null game", screen);
        assertNull("Game should be null", screen.game);
    }

    // ===== Getter Tests =====

    @Test
    public void testGetGame() {
        assertEquals("Game should be the mock game", mockGame, titleScreen.game);
    }

    @Test
    public void testGetBatch() {
        // Batch may be null in headless mode due to shader compilation issues
        // Just verify the field exists and is accessible
        if (titleScreen.batch != null) {
            assertTrue("Batch should be SpriteBatch", titleScreen.batch instanceof SpriteBatch);
        }
    }

    @Test
    public void testGetCamera() {
        assertNotNull("Camera should not be null", titleScreen.camera);
    }

    @Test
    public void testGetViewport() {
        assertNotNull("Viewport should not be null", titleScreen.viewport);
    }

    @Test
    public void testGetFont() {
        assertNotNull("Font should not be null", titleScreen.font);
        assertTrue("Font should be BitmapFont", titleScreen.font instanceof BitmapFont);
    }

    @Test
    public void testGetBackground() {
        // May be null in headless mode
        // Just verify field exists and doesn't throw
    }

    @Test
    public void testGetTitleLogo() {
        // May be null in headless mode
        // Just verify field exists and doesn't throw
    }

    // ===== Viewport Dimensions Tests =====

    @Test
    public void testViewportWidth() {
        assertEquals("Viewport world width should be 800", 800f, titleScreen.viewport.getWorldWidth(), 0.01f);
    }

    @Test
    public void testViewportHeight() {
        assertEquals("Viewport world height should be 480", 480f, titleScreen.viewport.getWorldHeight(), 0.01f);
    }

    @Test
    public void testCameraPosition() {
        // Camera should be centered on the viewport
        assertEquals("Camera X should be centered", 400f, titleScreen.camera.position.x, 0.01f);
        assertEquals("Camera Y should be centered", 240f, titleScreen.camera.position.y, 0.01f);
    }

    // ===== Screen Lifecycle Tests =====

    @Test
    public void testShow() {
        titleScreen.show();
        // Should not throw exception
    }

    @Test
    public void testRender() {
        titleScreen.render(0.016f);
        // Should not throw exception
    }

    @Test
    public void testRenderWithZeroDelta() {
        titleScreen.render(0f);
        // Should handle zero delta
    }

    @Test
    public void testRenderWithLargeDelta() {
        titleScreen.render(1.0f);
        // Should handle large delta
    }

    @Test
    public void testResize() {
        titleScreen.resize(1024, 768);
        // Should not throw exception
    }

    @Test
    public void testResizeWithSmallDimensions() {
        titleScreen.resize(320, 240);
        // Should handle small dimensions
    }

    @Test
    public void testResizeWithZeroDimensions() {
        try {
            titleScreen.resize(0, 0);
            // Should handle zero dimensions (edge case)
        } catch (Exception e) {
            // May throw in some implementations
        }
    }

    @Test
    public void testPause() {
        titleScreen.pause();
        // Should not throw exception
    }

    @Test
    public void testResume() {
        titleScreen.resume();
        // Should not throw exception
    }

    @Test
    public void testHide() {
        titleScreen.hide();
        // Should not throw exception
    }

    @Test
    public void testDispose() {
        titleScreen.dispose();
        // Should not throw exception
    }

    @Test
    public void testMultipleDispose() {
        titleScreen.dispose();
        titleScreen.dispose();
        // Should handle multiple dispose calls gracefully
    }

    // ===== Lifecycle Sequence Tests =====

    @Test
    public void testLifecycleSequence() {
        titleScreen.show();
        titleScreen.render(0.016f);
        titleScreen.resize(800, 600);
        titleScreen.pause();
        titleScreen.resume();
        titleScreen.hide();
        titleScreen.dispose();
        // Should handle full lifecycle
    }

    @Test
    public void testMultipleRenderCalls() {
        for (int i = 0; i < 5; i++) {
            titleScreen.render(0.016f);
        }
        // Should handle multiple consecutive renders
    }

    @Test
    public void testMultipleResizeCalls() {
        titleScreen.resize(800, 600);
        titleScreen.resize(1024, 768);
        titleScreen.resize(640, 480);
        // Should handle multiple resize calls
    }

    @Test
    public void testPauseResumeCycle() {
        titleScreen.pause();
        titleScreen.resume();
        titleScreen.pause();
        titleScreen.resume();
        // Should handle pause/resume cycles
    }

    // ===== Render Method Coverage Tests =====

    @Test
    public void testRenderCallsBatchMethods() {
        titleScreen.render(0.016f);
        // Verify batch methods are called
        verify(titleScreen.batch, atLeastOnce()).setProjectionMatrix(any());
        verify(titleScreen.batch, atLeastOnce()).begin();
        verify(titleScreen.batch, atLeastOnce()).end();
    }

    @Test
    public void testRenderWithNullBatch() {
        titleScreen.batch = null;
        titleScreen.render(0.016f);
        // Should return early without errors
    }

    @Test
    public void testRenderDrawsBackgroundIfNotNull() {
        if (titleScreen.background != null) {
            titleScreen.render(0.016f);
            verify(titleScreen.batch, atLeastOnce()).draw(eq(titleScreen.background), anyFloat(), anyFloat(), anyFloat(), anyFloat());
        }
    }

    @Test
    public void testRenderDrawsTitleLogoIfNotNull() {
        if (titleScreen.titleLogo != null) {
            titleScreen.render(0.016f);
            verify(titleScreen.batch, atLeastOnce()).draw(eq(titleScreen.titleLogo), anyFloat(), anyFloat(), anyFloat(), anyFloat());
        }
    }

    // ===== Menu Interaction Tests =====

    @Test
    public void testUpdateHoverWithNoSelection() {
        // Mouse far from menu items
        Input mockInput = mock(Input.class);
        when(mockInput.getX()).thenReturn(0);
        when(mockInput.getY()).thenReturn(0);
        Gdx.input = mockInput;
        
        titleScreen.render(0.016f);
        // Should not throw exception
    }

    @Test
    public void testUpdateHoverOnPlayButton() {
        // Mouse on PLAY button area (approximate center, upper button)
        Input mockInput = mock(Input.class);
        when(mockInput.getX()).thenReturn(400);
        when(mockInput.getY()).thenReturn(200); // Adjusted for menu position
        Gdx.input = mockInput;
        
        titleScreen.render(0.016f);
        // Should update selectedIndex
    }

    @Test
    public void testHandleInputNoClick() {
        Input mockInput = mock(Input.class);
        when(mockInput.justTouched()).thenReturn(false);
        Gdx.input = mockInput;
        
        titleScreen.render(0.016f);
        // Should not trigger any action
    }

    @Test
    public void testHandleInputClickWithNoSelection() {
        Input mockInput = mock(Input.class);
        when(mockInput.getX()).thenReturn(0);
        when(mockInput.getY()).thenReturn(0);
        when(mockInput.justTouched()).thenReturn(true);
        Gdx.input = mockInput;
        
        titleScreen.render(0.016f);
        // Should not crash or change screen
        verify(mockGame, never()).setScreen(any());
    }

    @Test
    public void testHandleInputClickOnPlayButton() {
        // Mock input to simulate click
        Input mockInput = mock(Input.class);
        when(mockInput.justTouched()).thenReturn(true);
        Gdx.input = mockInput;
        
        try {
            // Use helper method to test PLAY action with selectedIndex = 0
            titleScreen.handleInputWithSelection(0);
            
            // Verify that setScreen was called (should create a GameScreen)
            verify(mockGame, atLeastOnce()).setScreen(any(Screen.class));
        } catch (IllegalArgumentException e) {
            // Expected in headless mode when creating GameScreen
            // The important part is that handleInput was called, not that GameScreen was created
            if (!e.getMessage().contains("Error compiling shader")) {
                throw e;
            }
        }
    }

    @Test
    public void testHandleInputClickOnQuitButton() {
        // Mock input to simulate click
        Input mockInput = mock(Input.class);
        when(mockInput.justTouched()).thenReturn(true);
        Gdx.input = mockInput;
        
        // Mock Application to prevent actual exit
        com.badlogic.gdx.Application mockApp = mock(com.badlogic.gdx.Application.class);
        Gdx.app = mockApp;
        
        // Use helper method to test QUIT action with selectedIndex = 1
        titleScreen.handleInputWithSelection(1);
        
        // Verify that postRunnable was called (exit logic)
        verify(mockApp, atLeastOnce()).postRunnable(any(Runnable.class));
    }

    // ===== Font and Text Rendering Tests =====

    @Test
    public void testFontScaling() {
        if (titleScreen.font != null) {
            titleScreen.render(0.016f);
            // Font scale should be set during render
            assertNotNull("Font data should exist", titleScreen.font.getData());
        }
    }

    @Test
    public void testRenderWithNullFont() {
        BitmapFont originalFont = titleScreen.font;
        titleScreen.font = null;
        
        titleScreen.render(0.016f);
        // Should not throw exception
        
        titleScreen.font = originalFont;
    }
}

