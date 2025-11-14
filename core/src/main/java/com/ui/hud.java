package com.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    
    // Base health bars (verticales)
    private BaseHealthBar playerBaseHealthBar;
    private BaseHealthBar enemyBaseHealthBar;
    
    // Base icons
    private Texture playerBaseIcon;
    private Texture enemyBaseIcon;
    
    // Gold display
    private gold goldDisplay;
    
    // UI positions and dimensions
    private static final float HEALTH_BAR_X = 50f;
    private static final float HEALTH_BAR_Y = 550f;
    private static final float GOLD_X = 20f;
    private static final float GOLD_Y = 535f;
    
    // Base health bar positions and dimensions (barres verticales)
    private static final float BASE_HEALTH_BAR_WIDTH = 25f;   // Largeur réduite pour barre verticale
    private static final float BASE_HEALTH_BAR_HEIGHT = 200f; // Hauteur pour barre verticale
    
    // Ces valeurs seront calculées dynamiquement en fonction de la caméra et des bases
    private float playerBaseHealthBarX = 20f;
    private float playerBaseHealthBarY = 150f;  // Position verticale à côté de la base
    private float enemyBaseHealthBarX = 755f;
    private float enemyBaseHealthBarY = 150f;   // Position verticale à côté de la base
    
    // Base icon settings
    private static final float BASE_ICON_SIZE = 35f;
    
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
        healthBar = new healthbar(HEALTH_BAR_X, HEALTH_BAR_Y, 200f, 20f, "ui/heart.png"); // Hauteur à 20f
        
        // Initialize base health bars (verticales)
        playerBaseHealthBar = new BaseHealthBar(playerBaseHealthBarX, playerBaseHealthBarY, 
            BASE_HEALTH_BAR_WIDTH, BASE_HEALTH_BAR_HEIGHT);
        enemyBaseHealthBar = new BaseHealthBar(enemyBaseHealthBarX, enemyBaseHealthBarY, 
            BASE_HEALTH_BAR_WIDTH, BASE_HEALTH_BAR_HEIGHT);
        
        // Load base icons
        try {
            playerBaseIcon = new Texture("hero/Biowear All Frames Soldier idle Take Gun Shot Return Gun (11).png");
            enemyBaseIcon = new Texture("zombie/crawl/Walk1.png");
        } catch (Exception e) {
            System.err.println("Could not load base icons: " + e.getMessage());
            playerBaseIcon = null;
            enemyBaseIcon = null;
        }
        
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
     * Update base health bars
     * @param playerBaseHealth Current health of player base
     * @param playerBaseMaxHealth Maximum health of player base
     * @param enemyBaseHealth Current health of enemy base
     * @param enemyBaseMaxHealth Maximum health of enemy base
     */
    public void updateBaseHealth(int playerBaseHealth, int playerBaseMaxHealth, 
                                  int enemyBaseHealth, int enemyBaseMaxHealth) {
        playerBaseHealthBar.update(playerBaseHealth, playerBaseMaxHealth);
        enemyBaseHealthBar.update(enemyBaseHealth, enemyBaseMaxHealth);
    }
    
    /**
     * Update base health bar positions (appelé depuis GameScreen avec positions des bases)
     * @param playerBaseX Position X de la base du joueur (dans le monde)
     * @param playerBaseY Position Y de la base du joueur
     * @param enemyBaseX Position X de la base ennemie
     * @param enemyBaseY Position Y de la base ennemie
     * @param gameCamera Caméra du jeu pour convertir les coordonnées monde en UI
     */
    public void updateBaseHealthBarPositions(float playerBaseX, float playerBaseY,
                                              float enemyBaseX, float enemyBaseY,
                                              OrthographicCamera gameCamera) {
        // Barres verticales fixes de chaque côté de l'écran
        playerBaseHealthBarX = 20f;   // Fixe à gauche de l'écran
        playerBaseHealthBarY = 150f;  // Position verticale (bas de la barre)
        
        enemyBaseHealthBarX = 755f;   // Fixe à droite de l'écran
        enemyBaseHealthBarY = 150f;   // Position verticale (bas de la barre)
        
        // Mettre à jour les positions des barres de vie verticales
        playerBaseHealthBar.setPosition(playerBaseHealthBarX, playerBaseHealthBarY);
        enemyBaseHealthBar.setPosition(enemyBaseHealthBarX, enemyBaseHealthBarY);
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
        
        // Render base health bars verticales
        playerBaseHealthBar.render(shapeRenderer);
        enemyBaseHealthBar.render(shapeRenderer);
        
        // Render base icons à côté des barres verticales
        batch.begin();
        
        // Player base icon (en bas de la barre)
        if (playerBaseIcon != null) {
            batch.draw(playerBaseIcon, 
                playerBaseHealthBarX - 5, 
                playerBaseHealthBarY - BASE_ICON_SIZE - 10, 
                BASE_ICON_SIZE, BASE_ICON_SIZE);
        }
        
        // Enemy base icon (en bas de la barre)
        if (enemyBaseIcon != null) {
            batch.draw(enemyBaseIcon, 
                enemyBaseHealthBarX - 5, 
                enemyBaseHealthBarY - BASE_ICON_SIZE - 10, 
                BASE_ICON_SIZE, BASE_ICON_SIZE);
        }
        
        batch.end();
        
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
        playerBaseHealthBar.dispose();
        enemyBaseHealthBar.dispose();
        goldDisplay.dispose();
        
        // Dispose base icons
        if (playerBaseIcon != null) {
            playerBaseIcon.dispose();
        }
        if (enemyBaseIcon != null) {
            enemyBaseIcon.dispose();
        }
    }
}
