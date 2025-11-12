package com.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

/**
 * HealthBar component for displaying hero's health
 */
public class healthbar implements Disposable {
    
    // Position and dimensions
    private float x;
    private float y;
    private float width;
    private float height;
    
    // Health values
    private int currentHealth;
    private int maxHealth;
    
    // Visual components
    private BitmapFont font;
    private Texture heartIcon;
    private boolean hasIcon;
    private static final float ICON_SIZE = 28f;
    private static final float ICON_OFFSET = 35f;
    
    // Colors
    private static final Color BACKGROUND_COLOR = new Color(0.3f, 0.3f, 0.3f, 0.8f); // Dark gray
    private static final Color BORDER_COLOR = new Color(0.1f, 0.1f, 0.1f, 1f); // Dark border
    private static final Color HEALTH_COLOR = new Color(0.2f, 0.8f, 0.2f, 1f); // Green
    private static final Color LOW_HEALTH_COLOR = new Color(0.8f, 0.2f, 0.2f, 1f); // Red
    private static final Color MEDIUM_HEALTH_COLOR = new Color(0.9f, 0.7f, 0.2f, 1f); // Orange
    
    // Border thickness
    private static final float BORDER_THICKNESS = 2f;
    
    /**
     * Constructor for HealthBar without icon
     * @param x X position (top-left corner)
     * @param y Y position (top-left corner)
     * @param width Width of the health bar
     * @param height Height of the health bar
     */
    public healthbar(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currentHealth = 100;
        this.maxHealth = 100;
        this.hasIcon = false;
        
        // Initialize font for text display
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.2f);
    }
    
    /**
     * Constructor for HealthBar with heart icon
     * @param x X position (top-left corner)
     * @param y Y position (top-left corner)
     * @param width Width of the health bar
     * @param height Height of the health bar
     * @param heartIconPath Path to heart icon texture
     */
    public healthbar(float x, float y, float width, float height, String heartIconPath) {
        this(x, y, width, height);
        try {
            this.heartIcon = new Texture(heartIconPath);
            this.hasIcon = true;
        } catch (Exception e) {
            System.err.println("Could not load heart icon: " + heartIconPath);
            this.hasIcon = false;
        }
    }
    
    /**
     * Update health values
     * @param currentHealth Current health
     * @param maxHealth Maximum health
     */
    public void update(int currentHealth, int maxHealth) {
        this.currentHealth = Math.max(0, currentHealth); // Ensure non-negative
        this.maxHealth = Math.max(1, maxHealth); // Ensure at least 1 to avoid division by zero
    }
    
    /**
     * Render the health bar
     * @param shapeRenderer ShapeRenderer for drawing shapes
     */
    public void render(ShapeRenderer shapeRenderer) {
        // Calculate health percentage
        float healthPercentage = (float) currentHealth / maxHealth;
        float fillWidth = width * healthPercentage;
        
        // Determine health bar color based on percentage
        Color fillColor = getHealthColor(healthPercentage);
        
        // Begin shape rendering
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Draw background
        shapeRenderer.setColor(BACKGROUND_COLOR);
        shapeRenderer.rect(x, y, width, height);
        
        // Draw health fill
        shapeRenderer.setColor(fillColor);
        shapeRenderer.rect(x, y, fillWidth, height);
        
        shapeRenderer.end();
        
        // Draw border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(BORDER_COLOR);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }
    
    /**
     * Render the health bar with text and optional heart icon
     * @param shapeRenderer ShapeRenderer for drawing shapes
     * @param batch SpriteBatch for drawing text and icon
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        // Draw heart icon if available
        if (hasIcon && heartIcon != null) {
            batch.begin();
            batch.draw(heartIcon, x - ICON_OFFSET, y - 2, ICON_SIZE, ICON_SIZE);
            batch.end();
        }
        
        // Render the bar
        render(shapeRenderer);
        
        // Render health text
        batch.begin();
        String healthText = currentHealth + " / " + maxHealth;
        font.draw(batch, healthText, x + width + 10, y + height - 5);
        batch.end();
    }
    
    /**
     * Get health color based on percentage
     * @param percentage Health percentage (0.0 to 1.0)
     * @return Color for the health bar
     */
    private Color getHealthColor(float percentage) {
        if (percentage > 0.5f) {
            return HEALTH_COLOR; // Green
        } else if (percentage > 0.25f) {
            return MEDIUM_HEALTH_COLOR; // Orange
        } else {
            return LOW_HEALTH_COLOR; // Red
        }
    }
    
    /**
     * Set position of the health bar
     * @param x X position
     * @param y Y position
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Set dimensions of the health bar
     * @param width Width
     * @param height Height
     */
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
    
    /**
     * Get current health
     * @return Current health value
     */
    public int getCurrentHealth() {
        return currentHealth;
    }
    
    /**
     * Get maximum health
     * @return Maximum health value
     */
    public int getMaxHealth() {
        return maxHealth;
    }
    
    @Override
    public void dispose() {
        font.dispose();
        if (heartIcon != null) {
            heartIcon.dispose();
        }
    }
}
