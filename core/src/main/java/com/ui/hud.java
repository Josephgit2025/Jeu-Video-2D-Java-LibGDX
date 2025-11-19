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
/**
 * La classe hud gère l'affichage des éléments de l'interface utilisateur (HUD).
 * Elle affiche les informations essentielles comme la vie, l'or et les notifications.
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
    
    // Gold display
    private gold goldDisplay;
    
    // UI positions and dimensions
    private static final float HEALTH_BAR_X = 20f;
    private static final float HEALTH_BAR_Y = 525f; 
    private static final float GOLD_X = 40f;
    private static final float GOLD_Y = 535f;
    
    // Base health bar positions and dimensions (barres verticales)
    private static final float BASE_HEALTH_BAR_WIDTH = 8f;   // Largeur fine pour barre verticale
    private static final float BASE_HEALTH_BAR_HEIGHT = 150f; // Hauteur pour barre verticale
    
    // Ces valeurs seront calculées dynamiquement en fonction de la caméra et des bases
    private float playerBaseHealthBarX = 20f;
    private float playerBaseHealthBarY = 150f;  // Position verticale à côté de la base
    private float enemyBaseHealthBarX = 755f;
    private float enemyBaseHealthBarY = 150f;   // Position verticale à côté de la base
    
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
        
        // Initialize gold display with coin icon
        goldDisplay = new gold(GOLD_X, GOLD_Y, "ui/gold.png");
    }
    
    /**
     * Constructor for dependency injection (testing)
     */
    protected hud(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font,
                  healthbar healthBar, gold goldDisplay) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.font = font;
        this.healthBar = healthBar;
        this.goldDisplay = goldDisplay;
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
        // Barres verticales centrées horizontalement sur les bases
        float baseWidth = 96f;
        
        // Centrer horizontalement : posX + (largeur_base / 2) - (largeur_barre / 2)
        float centerOffsetX = (baseWidth / 2) - (BASE_HEALTH_BAR_WIDTH / 2);
        
        // Centrer verticalement

        float baseHeight = 300f;
        float centerOffsetY = (baseHeight / 2) - (BASE_HEALTH_BAR_HEIGHT / 2);
        
        // Position de la barre du joueur (centrée sur la base)
        playerBaseHealthBarX = playerBaseX + centerOffsetX;
        playerBaseHealthBarY = playerBaseY + centerOffsetY;
        
        // Position de la barre ennemie (centrée sur la base)
        enemyBaseHealthBarX = enemyBaseX + centerOffsetX;
        enemyBaseHealthBarY = enemyBaseY + centerOffsetY;
        
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
        
        // Set projection matrices for UI elements (health bar, gold)
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        
        // Render health bar with heart icon
        healthBar.render(shapeRenderer, batch);
        
        // Render gold display with coin icon
        goldDisplay.render(batch);
    }
    
    /**
     * Render base health bars in game world (with game camera)
     * @param gameCamera Camera from the game world
     */
    public void renderBaseHealthBars(OrthographicCamera gameCamera) {
        // Set projection matrix to game camera for world-space rendering
        shapeRenderer.setProjectionMatrix(gameCamera.combined);
        batch.setProjectionMatrix(gameCamera.combined);
        
        // Render base health bars verticales in game world
        playerBaseHealthBar.render(shapeRenderer);
        enemyBaseHealthBar.render(shapeRenderer);
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
    
    /**
     * Getter for goldDisplay
     * @return Current gold display object
     */
    public gold getGoldDisplay() {
        return goldDisplay;
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
    }
}
