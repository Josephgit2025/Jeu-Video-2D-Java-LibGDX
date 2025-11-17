package com.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

/**
 * Barre de vie verticale pour les bases (style isométrique)
 */
public class BaseHealthBar implements Disposable {
    
    // Position et dimensions
    private float x;
    private float y;
    private float width;
    private float height;
    
    // Valeurs de santé
    private int currentHealth;
    private int maxHealth;
    
    // Composants visuels
    private BitmapFont font;
    
    // Couleurs (style 2D pixel art)
    private static final Color BACKGROUND_COLOR = new Color(0.2f, 0.2f, 0.2f, 1f);      // Fond gris foncé
    private static final Color BORDER_COLOR = new Color(0f, 0f, 0f, 1f);          //  Noir
    private static final Color HEALTH_COLOR = new Color(0.1f, 0.9f, 0.1f, 1f);          // Vert vif
    private static final Color LOW_HEALTH_COLOR = new Color(0.9f, 0.1f, 0.1f, 1f);      // Rouge vif
    private static final Color MEDIUM_HEALTH_COLOR = new Color(1f, 0.6f, 0f, 1f);       // Orange vif
    
    // Épaisseur de la bordure
    private static final float BORDER_THICKNESS = 4f;
    
    /**
     * Constructeur
     * @param x Position X (coin bas-gauche)
     * @param y Position Y (coin bas-gauche)
     * @param width Largeur de la barre
     * @param height Hauteur de la barre
     */
    public BaseHealthBar(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currentHealth = 1000;
        this.maxHealth = 1000;
        
        // Initialiser la font
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(0.9f);
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
     * Render la barre de vie verticale
     * @param shapeRenderer ShapeRenderer pour dessiner
     */
    public void render(ShapeRenderer shapeRenderer) {
        // Calculer le pourcentage de santé
        float healthPercentage = (float) currentHealth / maxHealth;
        float fillHeight = height * healthPercentage;
        
        // Déterminer la couleur
        Color fillColor = getHealthColor(healthPercentage);
        
        // Commencer le rendu
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Bordure noire
        shapeRenderer.setColor(BORDER_COLOR);
        shapeRenderer.rect(x - BORDER_THICKNESS, y - BORDER_THICKNESS, 
            width + BORDER_THICKNESS * 2, height + BORDER_THICKNESS * 2);
        
        // Fond gris
        shapeRenderer.setColor(BACKGROUND_COLOR);
        shapeRenderer.rect(x, y, width, height);
        
        // Remplissage de santé (de bas en haut)
        shapeRenderer.setColor(fillColor);
        shapeRenderer.rect(x, y, width, fillHeight);
        
        shapeRenderer.end();
    }
    
    /**
     * Obtenir la couleur en fonction du pourcentage de santé
     */
    private Color getHealthColor(float percentage) {
        if (percentage > 0.5f) {
            return HEALTH_COLOR; // Vert
        } else if (percentage > 0.25f) {
            return MEDIUM_HEALTH_COLOR; // Orange
        } else {
            return LOW_HEALTH_COLOR; // Rouge
        }
    }
    
    /**
     * Définir la position
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Définir les dimensions
     */
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void dispose() {
        font.dispose();
    }
}
