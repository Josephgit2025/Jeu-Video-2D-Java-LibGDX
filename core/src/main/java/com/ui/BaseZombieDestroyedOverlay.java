package com.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Overlay affiché quand la base du joueur est détruite
 */
public class BaseZombieDestroyedOverlay implements Disposable {
    
    private Viewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    
    // Dimensions de l'overlay
    private static final float OVERLAY_WIDTH = 800f;
    private static final float OVERLAY_HEIGHT = 600f;
    
    // Fonts
    private BitmapFont titleFont;
    
    // Textures
    private Texture explosionIcon;
    
    // Layout pour le texte
    private GlyphLayout titleLayout;
    
    /**
     * Constructeur
     */
    public BaseZombieDestroyedOverlay() {
        viewport = new FitViewport(OVERLAY_WIDTH, OVERLAY_HEIGHT);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        
        // Charger l'icône d'explosion
        try {
            explosionIcon = new Texture("ui/explosion.png");
        } catch (Exception e) {
            System.err.println("Could not load explosion icon, trying alternative...");
            try {
                // Essayer une alternative si l'explosion n'existe pas
                explosionIcon = new Texture("ui/heart.png"); // Placeholder
            } catch (Exception ex) {
                System.err.println("Could not load any icon");
                explosionIcon = null;
            }
        }
        
        // Initialiser le font pour le titre
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 36;
            parameter.color = Color.RED;
            parameter.borderWidth = 3f;
            parameter.borderColor = Color.BLACK;
            parameter.shadowOffsetX = 3;
            parameter.shadowOffsetY = 3;
            parameter.shadowColor = new Color(0, 0, 0, 0.5f);
            
            titleFont = generator.generateFont(parameter);
            generator.dispose();
        } catch (Exception e) {
            System.err.println("Could not load PressStart2P font: " + e.getMessage());
            titleFont = new BitmapFont();
            titleFont.setColor(Color.RED);
            titleFont.getData().setScale(3f);
        }
        
        titleLayout = new GlyphLayout();
    }
    
    /**
     * Render l'overlay de base détruite
     */
    public void render() {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        
        // Utiliser les dimensions réelles du viewport
        float screenWidth = viewport.getWorldWidth();
        float screenHeight = viewport.getWorldHeight();
        
        // Fond semi-transparent noir
        Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(0, 0, screenWidth, screenHeight);
        shapeRenderer.end();
        
        Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
        
        batch.begin();
        
        // Titre "destruction de la base des zombies"
        String title = "CONGRATULATIONS! YOU HAVE DESTROYED THE ZOMBIES BASE";
        titleLayout.setText(titleFont, title);
        
        // Adapter l'échelle de la font si le texte est trop large
        float scale = 1f;
        float maxWidth = screenWidth * 0.85f; // 85% de la largeur de l'écran
        if (titleLayout.width > maxWidth) {
            scale = maxWidth / titleLayout.width;
            titleFont.getData().setScale(scale);
            titleLayout.setText(titleFont, title);
        }
        
        float titleX = (screenWidth - titleLayout.width) / 2f;
        float titleY = screenHeight / 2f + 100f;
        
        titleFont.draw(batch, title, titleX, titleY);
        
        // Restaurer l'échelle originale
        if (scale != 1f) {
            titleFont.getData().setScale(1f);
        }
        
        // Icône d'explosion à côté du texte (adapter la taille à l'écran)
        if (explosionIcon != null) {
            float iconSize = Math.min(80f, screenHeight * 0.12f); // Adapter la taille de l'icône
            float iconX = titleX + titleLayout.width + 20f; // À droite du texte
            float iconY = titleY - iconSize / 2f - titleLayout.height / 2f;
            
            batch.draw(explosionIcon, iconX, iconY, iconSize, iconSize);
        }
        
        batch.end();
    }
    
    /**
     * Resize le viewport
     */
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        titleFont.dispose();
        if (explosionIcon != null) {
            explosionIcon.dispose();
        }
    }
}
