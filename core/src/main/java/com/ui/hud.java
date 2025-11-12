package com.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * HUD (Heads-Up Display) class for displaying game UI elements
 * such as health bar and gold counter.
 */
public class hud implements Disposable {
    
    // Viewport and camera for UI rendering
    private Viewport viewport;
    private OrthographicCamera camera;
    
    // Rendering components
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    
    // Health bar component
    private healthbar healthBar;
    
    // Gold display
    private gold goldDisplay;
    
    // UI positions and dimensions
    private static final float HEALTH_BAR_X = 50f;
    private static final float HEALTH_BAR_Y = 550f;
    private static final float GOLD_X = 50f;
    private static final float GOLD_Y = 515f;
    
    /**
     * Constructor for HUD
     */
    public hud() {
        // Initialize camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        
        // Initialize rendering components
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont(); // Default font, you can load custom font here
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);
        
        // Initialize health bar with heart icon
        healthBar = new healthbar(HEALTH_BAR_X, HEALTH_BAR_Y, 200f, 30f, "ui/heart.png");
        
        // Initialize gold display with coin icon
        goldDisplay = new gold(GOLD_X, GOLD_Y, "ui/gold.png");
    }
    
    /**
     * Update HUD with current player stats
     * @param currentHealth Current health of the hero
     * @param maxHealth Maximum health of the hero
     * @param currentGold Current gold amount
     */
    public void update(int currentHealth, int maxHealth, int currentGold) {
        healthBar.update(currentHealth, maxHealth);
        goldDisplay.update(currentGold);
    }
    
    /**
     * Render the HUD
     */
    public void render() {
        // Update camera
        camera.update();
        
        // Set projection matrices
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        
        // Render health bar with heart icon
        healthBar.render(shapeRenderer, batch);
        
        // Render gold display with coin icon
        goldDisplay.render(batch);
    }
    
    /**
     * Resize the viewport when window size changes
     * @param width New width
     * @param height New height
     */
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
    }
    
    /**
     * Add gold to the current amount
     * @param amount Amount to add
     */
    public void addGold(int amount) {
        goldDisplay.addGold(amount);
    }
    
    /**
     * Remove gold from the current amount
     * @param amount Amount to remove
     * @return true if successful, false if not enough gold
     */
    public boolean removeGold(int amount) {
        return goldDisplay.removeGold(amount);
    }
    
    /**
     * Get current gold amount
     * @return Current gold
     */
    public int getGold() {
        return goldDisplay.getGold();
    }
    
    /**
     * Set gold amount directly
     * @param gold Gold amount to set
     */
    public void setGold(int gold) {
        goldDisplay.setGold(gold);
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        healthBar.dispose();
        goldDisplay.dispose();
    }
}
