package com.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverOverlay implements Disposable {
    
    // Viewport and camera for overlay rendering (fixed on screen)
    private Viewport viewport;
    private OrthographicCamera camera;
    
    // Rendering components
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    
    // Button rectangles for click detection
    private Rectangle replayButton;
    private Rectangle quitButton;
    
    // UI dimensions
    private static final float OVERLAY_WIDTH = 800f;
    private static final float OVERLAY_HEIGHT = 600f;
    private static final float BUTTON_WIDTH = 200f;
    private static final float BUTTON_HEIGHT = 60f;
    
    public GameOverOverlay() {
        // Initialize camera and viewport (fixed overlay)
        camera = new OrthographicCamera();
        viewport = new FitViewport(OVERLAY_WIDTH, OVERLAY_HEIGHT, camera);
        camera.position.set(OVERLAY_WIDTH / 2, OVERLAY_HEIGHT / 2, 0);
        
        // Initialize rendering components
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        
        // Initialize fonts
        titleFont = new BitmapFont();
        titleFont.setColor(Color.RED);
        titleFont.getData().setScale(3f);
        
        buttonFont = new BitmapFont();
        buttonFont.setColor(Color.WHITE);
        buttonFont.getData().setScale(2f);
        
        // Initialize button rectangles (centered)
        float centerX = OVERLAY_WIDTH / 2;
        float centerY = OVERLAY_HEIGHT / 2;
        
        replayButton = new Rectangle(
            centerX - BUTTON_WIDTH / 2, 
            centerY - 20, 
            BUTTON_WIDTH, 
            BUTTON_HEIGHT
        );
        
        quitButton = new Rectangle(
            centerX - BUTTON_WIDTH / 2, 
            centerY - 100, 
            BUTTON_WIDTH, 
            BUTTON_HEIGHT
        );
    }

    public void render() {
        // Update camera
        camera.update();
        
        // Set projection matrices
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        
        // Draw semi-transparent background
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f); // Black with 70% opacity
        shapeRenderer.rect(0, 0, OVERLAY_WIDTH, OVERLAY_HEIGHT);
        shapeRenderer.end();
        
        // Draw central panel
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.9f); // Dark gray
        shapeRenderer.rect(
            OVERLAY_WIDTH / 2 - 300, 
            OVERLAY_HEIGHT / 2 - 150, 
            600, 
            300
        );
        shapeRenderer.end();
        
        // Draw buttons
        drawButton(replayButton, Color.GREEN);
        drawButton(quitButton, Color.RED);
        
        Gdx.gl.glDisable(GL20.GL_BLEND);
        
        // Draw text
        batch.begin();
        
        // Title: "Votre Héros est mort"
        titleFont.draw(batch, "Game Over", 
            OVERLAY_WIDTH / 2 - 250, 
            OVERLAY_HEIGHT / 2 + 120
        );
        
        // Button labels
        buttonFont.draw(batch, "REPLAY", 
            replayButton.x + 50, 
            replayButton.y + 40
        );
        
        buttonFont.draw(batch, "QUIT", 
            quitButton.x + 70, 
            quitButton.y + 40
        );
        
        batch.end();
    }
    
    private void drawButton(Rectangle button, Color color) {
        // Draw button background
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(color.r, color.g, color.b, 0.6f);
        shapeRenderer.rect(button.x, button.y, button.width, button.height);
        shapeRenderer.end();
        
        // Draw button border
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(button.x, button.y, button.width, button.height);
        shapeRenderer.end();
    }
    
    /**
     * Handle click on overlay
     * @param screenX Screen X coordinate
     * @param screenY Screen Y coordinate
     * @return "replay" if replay button clicked, "quit" if quit button clicked, null otherwise
     */
    public String handleClick(int screenX, int screenY) {
        // Convert screen coordinates to viewport coordinates
        float worldX = screenX * (OVERLAY_WIDTH / Gdx.graphics.getWidth());
        float worldY = (Gdx.graphics.getHeight() - screenY) * (OVERLAY_HEIGHT / Gdx.graphics.getHeight());
        
        if (replayButton.contains(worldX, worldY)) {
            return "replay";
        }
        
        if (quitButton.contains(worldX, worldY)) {
            return "quit";
        }
        
        return null;
    }
    
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(OVERLAY_WIDTH / 2, OVERLAY_HEIGHT / 2, 0);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        titleFont.dispose();
        buttonFont.dispose();
    }
}