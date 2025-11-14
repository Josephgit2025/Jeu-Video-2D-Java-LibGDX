package com.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

/**
 * GoldDisplay component for displaying player's gold/currency
 */
public class gold implements Disposable {
    
    // Position
    private float x;
    private float y;
    
    // Gold value
    private int gold;
    
    // Visual components
    private BitmapFont font;
    private Texture coinIcon; // Optional: icon for gold
    private boolean hasIcon;
    
    // Display settings
    private static final float ICON_SIZE = 26f;
    private static final float TEXT_OFFSET_X = 32f; // Offset for text if icon is present
    
    // Colors
    private static final Color GOLD_COLOR = new Color(1f, 0.84f, 0f, 1f); // Gold color
    private static final Color BACKGROUND_COLOR = new Color(0.2f, 0.2f, 0.2f, 0.7f); // Semi-transparent background
    
    // Background padding
    private static final float PADDING_X = 10f;
    private static final float PADDING_Y = 8f;
    
    /**
     * Constructor for GoldDisplay without icon
     * @param x X position
     * @param y Y position
     */
    public gold(float x, float y) {
        this.x = x;
        this.y = y;
        this.gold = 0;
        this.hasIcon = false;
        
        // Initialize font
        font = new BitmapFont();
        font.setColor(GOLD_COLOR);
        font.getData().setScale(1.5f);
    }
    
    /**
     * Constructor for GoldDisplay with icon
     * @param x X position
     * @param y Y position
     * @param coinIconPath Path to the coin icon texture
     */
    public gold(float x, float y, String coinIconPath) {
        this(x, y);
        try {
            this.coinIcon = new Texture(coinIconPath);
            this.hasIcon = true;
        } catch (Exception e) {
            System.err.println("Could not load coin icon: " + coinIconPath);
            this.hasIcon = false;
        }
    }
    
    /**
     * Update gold value
     * @param gold Current gold amount
     */
    public void update(int gold) {
        this.gold = Math.max(0, gold); // Ensure non-negative
    }
    
    /**
     * Render the gold display
     * @param batch SpriteBatch for rendering
     */
    public void render(SpriteBatch batch) {
        batch.begin();
        
        float textX = x;
        
        // Draw coin icon if available (aligned vertically with text)
        if (hasIcon && coinIcon != null) {
            // Calculate vertical center alignment with the text
            float iconY = y - font.getLineHeight() / 2 - ICON_SIZE / 2 + 2;
            batch.draw(coinIcon, x, iconY, ICON_SIZE, ICON_SIZE);
            textX += TEXT_OFFSET_X;
        }
        
        // Draw gold text (sans le label "Gold:")
        String goldText = ": " + gold;
        font.draw(batch, goldText, textX, y);
        
        batch.end();
    }
    
    /**
     * Render the gold display with background
     * @param shapeRenderer ShapeRenderer for background
     * @param batch SpriteBatch for text and icon
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        // Calculate background dimensions
        String goldText = "Gold: " + gold;
        float textWidth = font.getRegion().getRegionWidth() * goldText.length() * 0.4f; // Approximate
        float textHeight = font.getLineHeight();
        
        float bgWidth = textWidth + PADDING_X * 2;
        if (hasIcon) {
            bgWidth += TEXT_OFFSET_X;
        }
        float bgHeight = textHeight + PADDING_Y * 2;
        
        // Draw background
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(BACKGROUND_COLOR);
        shapeRenderer.rect(x - PADDING_X, y - textHeight + PADDING_Y, bgWidth, bgHeight);
        shapeRenderer.end();
        
        // Draw foreground (icon + text)
        render(batch);
    }
    
    /**
     * Set position of the gold display
     * @param x X position
     * @param y Y position
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Add gold to current amount
     * @param amount Amount to add
     */
    public void addGold(int amount) {
        // this.gold += amount;
        // if (this.gold <= 0)
        //     this.gold = 0;
        this.setGold(this.gold + amount);
    }
    
    /**
     * Remove gold from current amount
     * @param amount Amount to remove
     * @return true if successful, false if not enough gold
     */
    public boolean removeGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }
    
    /**
     * Set gold amount directly
     * @param gold Gold amount
     */
    public void setGold(int gold) {
        this.gold = Math.max(0, gold);
    }
    
    /**
     * Get current gold amount
     * @return Current gold
     */
    public int getGold() {
        return gold;
    }
    
    /**
     * Set font color
     * @param color New color for the font
     */
    public void setColor(Color color) {
        font.setColor(color);
    }
    
    /**
     * Set font scale
     * @param scale Font scale
     */
    public void setFontScale(float scale) {
        if (scale > 0f)
            font.getData().setScale(scale);
    }
    
    @Override
    public void dispose() {
        font.dispose();
        if (coinIcon != null) {
            coinIcon.dispose();
        }
    }
}
