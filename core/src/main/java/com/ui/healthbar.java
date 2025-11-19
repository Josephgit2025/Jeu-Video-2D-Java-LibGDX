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
    
    // New healthbar texture (image complète)
    private Texture healthbarTexture;
    private boolean useCustomTexture = false;
    
    // Dimensions de l'image healthbar_modif.png (à ajuster selon votre image)
    private static final int HEART_WIDTH_IN_IMAGE = 80;  // Largeur du cœur dans l'image
    private static final int BAR_START_X = 80;           // Début de la barre après le cœur
    
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
        
        // Charger l'icône de cœur d'abord
        try {
            this.heartIcon = new Texture(heartIconPath);
            this.hasIcon = true;
            System.out.println("✅ Icône de cœur chargée: " + heartIconPath);
        } catch (Exception e) {
            System.err.println("Could not load heart icon: " + heartIconPath);
            this.hasIcon = false;
        }
        
        // Essayer de charger la texture de barre personnalisée
        try {
            this.healthbarTexture = new Texture(Gdx.files.internal("ui/healthbar.png"));
            this.useCustomTexture = true;
            System.out.println("✅ Texture de barre personnalisée chargée: healthbar.png");
        } catch (Exception e) {
            System.out.println("⚠️ Texture personnalisée non trouvée, utilisation du style par défaut");
            this.useCustomTexture = false;
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
        // Si on utilise la texture personnalisée
        if (useCustomTexture && healthbarTexture != null) {
            batch.begin();
            renderCustomTexture(batch);
            batch.end();
        } else {
            // Fallback: ancien style avec ShapeRenderer
            if (hasIcon && heartIcon != null) {
                batch.begin();
                batch.draw(heartIcon, x - ICON_OFFSET, y + (height - ICON_SIZE) / 2, ICON_SIZE, ICON_SIZE);
                batch.end();
            }
            render(shapeRenderer);
        }
    }
    
    /**
     * Render avec la texture personnalisée (healthbar.png)
     */
    private void renderCustomTexture(SpriteBatch batch) {
        float healthPercentage = (float) currentHealth / maxHealth;
        
        // Taille réduite mais bien visible
        float scale = 0.10f;
        float totalWidth = healthbarTexture.getWidth() * scale;
        float totalHeight = healthbarTexture.getHeight() * scale;
        
        // j'ai dessiner l'icône heart.png juste à côté de la barre
        if (hasIcon && heartIcon != null) {
            float heartSize = totalHeight * 1.2f;
            float heartOffset = 5f;
            batch.setColor(1f, 1f, 1f, 1f);
            batch.draw(heartIcon, x - heartSize - heartOffset, y, heartSize, heartSize);
        }
        
        // j'ai dessiner la barre complète
        batch.setColor(0.5f, 0.5f, 0.5f, 1f);
        batch.draw(healthbarTexture,
            x, y,
            totalWidth, totalHeight,
            0, 0,
            healthbarTexture.getWidth(), healthbarTexture.getHeight(),
            false, false);
        
        // j'ai dessiner le remplissage vert par-dessus
        if (healthPercentage > 0) {
            batch.setColor(1f, 1f, 1f, 1f);
            float fillWidth = totalWidth * healthPercentage;
            int sourceFillWidth = (int)(healthbarTexture.getWidth() * healthPercentage);
            
            batch.draw(healthbarTexture,
                x, y,
                fillWidth, totalHeight,
                0, 0,
                sourceFillWidth, healthbarTexture.getHeight(),
                false, false);
        }
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
        if (healthbarTexture != null) {
            healthbarTexture.dispose();
        }
    }
}
