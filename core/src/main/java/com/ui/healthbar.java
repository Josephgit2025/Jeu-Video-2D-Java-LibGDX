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
    private static final float ICON_SIZE = 26f;
    private static final float ICON_OFFSET = 32f;
    
    // Colors (style jeu 2D)
    private static final Color BACKGROUND_COLOR = new Color(0.2f, 0.2f, 0.2f, 1f);      // Fond gris foncé (style 2D)
    private static final Color BORDER_COLOR = new Color(0.4f, 0.4f, 0.4f, 1f);          // Gris foncé
    private static final Color HEALTH_COLOR = new Color(0.1f, 0.9f, 0.1f, 1f);          // Vert vif
    private static final Color LOW_HEALTH_COLOR = new Color(0.9f, 0.1f, 0.1f, 1f);      // Rouge vif
    private static final Color MEDIUM_HEALTH_COLOR = new Color(1f, 0.6f, 0f, 1f);       // Orange vif
    
    // Border thickness (plus épais pour style isométrique)
    private static final float BORDER_THICKNESS = 4f;
    
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
     * Met à jour les valeurs de santé
     * @param currentHealth Vie actuelle
     * @param maxHealth Vie maximale
     */
    public void update(int currentHealth, int maxHealth) {
        this.currentHealth = Math.max(0, currentHealth);
        this.maxHealth = Math.max(1, maxHealth);
    }
    
    /**
     * @param shapeRenderer ShapeRenderer pour dessiner les formes
     */
    public void render(ShapeRenderer shapeRenderer) {
        // Calculer le pourcentage de santé
        float healthPercentage = (float) currentHealth / maxHealth;
        float fillWidth = width * healthPercentage;
        
        // Déterminer la couleur de la barre de santé en fonction du pourcentage
        Color fillColor = getHealthColor(healthPercentage);
        
        // Commencer le rendu des formes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Draw thick black border (outer rectangle) - style pixel art
        shapeRenderer.setColor(BORDER_COLOR);
        shapeRenderer.rect(x - BORDER_THICKNESS, y - BORDER_THICKNESS, 
            width + BORDER_THICKNESS * 2, height + BORDER_THICKNESS * 2);
        
        // Draw white/gray background
        shapeRenderer.setColor(BACKGROUND_COLOR);
        shapeRenderer.rect(x, y, width, height);
        
        // Draw health fill (yellow/orange/red/green)
        shapeRenderer.setColor(fillColor);
        shapeRenderer.rect(x, y, fillWidth, height);
        
        shapeRenderer.end();
    }
    
    /**
     * Render the health bar with optional heart icon (without text)
     * @param shapeRenderer ShapeRenderer for drawing shapes
     * @param batch SpriteBatch for drawing icon
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        // Draw heart icon if available (aligned vertically with the bar)
        if (hasIcon && heartIcon != null) {
            batch.begin();
            batch.draw(heartIcon, x - ICON_OFFSET, y + (height - ICON_SIZE) / 2, ICON_SIZE, ICON_SIZE);
            batch.end();
        }
        
        // Render the bar only (no text)
        render(shapeRenderer);
    }
    
    /**
     @param percentage 
     @return 
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
