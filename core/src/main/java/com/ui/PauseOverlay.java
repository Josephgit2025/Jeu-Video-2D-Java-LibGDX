package com.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Overlay affiché quand le jeu est en pause
 */
public class PauseOverlay implements Disposable {
    
    private Viewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    
    // Dimensions de l'overlay
    private static final float OVERLAY_WIDTH = 800f;
    private static final float OVERLAY_HEIGHT = 600f;
    
    // Fonts
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    private BitmapFont confirmFont;
    
    // Layout pour le texte
    private GlyphLayout titleLayout;
    
    // Button rectangles for click detection
    private Rectangle resumeButton;
    private Rectangle quitButton;
    private Rectangle yesButton;
    private Rectangle noButton;
    
    // Hover effect
    private int selectedIndex = -1; // -1 = rien, 0 = RESUME, 1 = QUIT (ou YES/NO en mode confirmation)
    
    // Button dimensions
    private static final float BUTTON_WIDTH = 200f;
    private static final float BUTTON_HEIGHT = 60f;
    
    // Confirmation state
    private boolean showConfirmation = false;
    
    /**
     * Constructeur
     */
    public PauseOverlay() {
        viewport = new FitViewport(OVERLAY_WIDTH, OVERLAY_HEIGHT);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        
        // Initialiser le font pour le titre
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 48;
            parameter.color = Color.WHITE;
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
            titleFont.setColor(Color.WHITE);
            titleFont.getData().setScale(4f);
        }
        
        titleLayout = new GlyphLayout();
        
        // Initialize button font (same style as GameOverOverlay)
        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter buttonParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        buttonParam.size = 32;
        buttonParam.borderWidth = 2.5f;
        buttonParam.borderColor = new Color(0f, 0f, 0f, 0.85f);
        buttonParam.shadowOffsetX = 2;
        buttonParam.shadowOffsetY = -2;
        buttonParam.shadowColor = new Color(0f, 0f, 0f, 0.7f);
        buttonParam.magFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest;
        buttonParam.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest;
        buttonFont = generator2.generateFont(buttonParam);
        generator2.dispose();
        
        // Initialize confirmation font (smaller)
        FreeTypeFontGenerator generator3 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter confirmParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        confirmParam.size = 24;
        confirmParam.borderWidth = 2f;
        confirmParam.borderColor = new Color(0f, 0f, 0f, 0.85f);
        confirmParam.shadowOffsetX = 2;
        confirmParam.shadowOffsetY = -2;
        confirmParam.shadowColor = new Color(0f, 0f, 0f, 0.7f);
        confirmParam.magFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest;
        confirmParam.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest;
        confirmFont = generator3.generateFont(confirmParam);
        generator3.dispose();
        
        // Initialize button rectangles (centered below the title)
        float centerX = OVERLAY_WIDTH / 2;
        float centerY = OVERLAY_HEIGHT / 2 - 50;
        
        resumeButton = new Rectangle(
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
        
        // Confirmation buttons (YES/NO)
        yesButton = new Rectangle(
            centerX - BUTTON_WIDTH - 20, 
            centerY - 150, 
            BUTTON_WIDTH, 
            BUTTON_HEIGHT
        );
        
        noButton = new Rectangle(
            centerX + 20, 
            centerY - 150, 
            BUTTON_WIDTH, 
            BUTTON_HEIGHT
        );
    }
    
    /**
     * Render l'overlay de pause
     */
    public void render() {
        // Update hover effect
        updateHover();
        
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        
        // Utiliser les dimensions réelles du viewport
        float screenWidth = viewport.getWorldWidth();
        float screenHeight = viewport.getWorldHeight();
        
        // Fond semi-transparent noir
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(0, 0, screenWidth, screenHeight);
        shapeRenderer.end();
        
        Gdx.gl.glDisable(GL20.GL_BLEND);
        
        batch.begin();
        
        if (!showConfirmation) {
            // Titre "PAUSED"
            String title = "PAUSED";
            titleLayout.setText(titleFont, title);
            
            float titleX = (screenWidth - titleLayout.width) / 2f;
            float titleY = screenHeight / 2f + 150f;
            
            titleFont.draw(batch, title, titleX, titleY);
            
            // Draw buttons RESUME and QUIT with hover effect
            GlyphLayout resumeLayout = new GlyphLayout(buttonFont, "RESUME");
            float resumeX = (screenWidth - resumeLayout.width) / 2f;
            if (selectedIndex == 0) {
                drawTextInverted("RESUME", resumeX, resumeButton.y + 40);
            } else {
                buttonFont.setColor(Color.WHITE);
                buttonFont.draw(batch, resumeLayout, resumeX, resumeButton.y + 40);
            }

            GlyphLayout quitLayout = new GlyphLayout(buttonFont, "QUIT");
            float quitX = (screenWidth - quitLayout.width) / 2f;
            if (selectedIndex == 1) {
                drawTextInverted("QUIT", quitX, quitButton.y + 40);
            } else {
                buttonFont.setColor(Color.WHITE);
                buttonFont.draw(batch, quitLayout, quitX, quitButton.y + 40);
            }
        } else {
            // Confirmation screen
            String confirmText = "RETURN TO MENU ?";
            GlyphLayout confirmLayout = new GlyphLayout(confirmFont, confirmText);
            float confirmX = (screenWidth - confirmLayout.width) / 2f;
            float confirmY = screenHeight / 2f + 100f;
            
            confirmFont.setColor(Color.YELLOW);
            confirmFont.draw(batch, confirmText, confirmX, confirmY);
            
            // Draw YES and NO buttons
            GlyphLayout yesLayout = new GlyphLayout(buttonFont, "YES");
            float yesX = yesButton.x + (BUTTON_WIDTH - yesLayout.width) / 2f;
            if (selectedIndex == 0) {
                drawTextInverted("YES", yesX, yesButton.y + 40);
            } else {
                buttonFont.setColor(Color.WHITE);
                buttonFont.draw(batch, yesLayout, yesX, yesButton.y + 40);
            }

            GlyphLayout noLayout = new GlyphLayout(buttonFont, "NO");
            float noX = noButton.x + (BUTTON_WIDTH - noLayout.width) / 2f;
            if (selectedIndex == 1) {
                drawTextInverted("NO", noX, noButton.y + 40);
            } else {
                buttonFont.setColor(Color.WHITE);
                buttonFont.draw(batch, noLayout, noX, noButton.y + 40);
            }
        }
        
        batch.end();
    }
    
    /**
     * Draw text with inverted effect (yellow background, black text)
     */
    private void drawTextInverted(String text, float x, float y) {
        GlyphLayout layout = new GlyphLayout(buttonFont, text);

        // Ombre jaune/dorée (plus épaisse pour effet de fond)
        buttonFont.setColor(Color.YELLOW);
        for (int offsetX = -2; offsetX <= 2; offsetX++) {
            for (int offsetY = -2; offsetY <= 2; offsetY++) {
                buttonFont.draw(batch, layout, x + offsetX, y + offsetY);
            }
        }

        // Texte noir par-dessus
        buttonFont.setColor(Color.BLACK);
        buttonFont.draw(batch, layout, x, y);
    }
    
    /**
     * Update hover effect for buttons
     */
    private void updateHover() {
        Vector2 mouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        float mx = mouse.x;
        float my = mouse.y;

        selectedIndex = -1;

        if (!showConfirmation) {
            // RESUME button
            GlyphLayout resumeLayout = new GlyphLayout(buttonFont, "RESUME");
            float resumeX = (OVERLAY_WIDTH - resumeLayout.width) / 2f;
            
            Rectangle resumeHitbox = new Rectangle(
                resumeX - 10, 
                resumeButton.y + 40 - resumeLayout.height - 5,
                resumeLayout.width + 20,
                resumeLayout.height + 10
            );
            
            if (resumeHitbox.contains(mx, my)) {
                selectedIndex = 0;
            }

            // QUIT button
            GlyphLayout quitLayout = new GlyphLayout(buttonFont, "QUIT");
            float quitX = (OVERLAY_WIDTH - quitLayout.width) / 2f;
            
            Rectangle quitHitbox = new Rectangle(
                quitX - 10,
                quitButton.y + 40 - quitLayout.height - 5,
                quitLayout.width + 20,
                quitLayout.height + 10
            );
            
            if (quitHitbox.contains(mx, my)) {
                selectedIndex = 1;
            }
        } else {
            // YES button
            GlyphLayout yesLayout = new GlyphLayout(buttonFont, "YES");
            float yesX = yesButton.x + (BUTTON_WIDTH - yesLayout.width) / 2f;
            
            Rectangle yesHitbox = new Rectangle(
                yesX - 10, 
                yesButton.y + 40 - yesLayout.height - 5,
                yesLayout.width + 20,
                yesLayout.height + 10
            );
            
            if (yesHitbox.contains(mx, my)) {
                selectedIndex = 0;
            }

            // NO button
            GlyphLayout noLayout = new GlyphLayout(buttonFont, "NO");
            float noX = noButton.x + (BUTTON_WIDTH - noLayout.width) / 2f;
            
            Rectangle noHitbox = new Rectangle(
                noX - 10,
                noButton.y + 40 - noLayout.height - 5,
                noLayout.width + 20,
                noLayout.height + 10
            );
            
            if (noHitbox.contains(mx, my)) {
                selectedIndex = 1;
            }
        }
    }
    
    /**
     * Handle click on overlay
     * @param screenX Screen X coordinate
     * @param screenY Screen Y coordinate
     * @return "resume" if resume clicked, "quit" if quit confirmed, "cancel" if cancelled, "confirm" if asking for confirmation, null otherwise
     */
    public String handleClick(int screenX, int screenY) {
        Vector2 mouse = viewport.unproject(new Vector2(screenX, screenY));
        float worldX = mouse.x;
        float worldY = mouse.y;
        
        if (!showConfirmation) {
            // Check RESUME button
            GlyphLayout resumeLayout = new GlyphLayout(buttonFont, "RESUME");
            float resumeX = (OVERLAY_WIDTH - resumeLayout.width) / 2f;
            
            Rectangle resumeHitbox = new Rectangle(
                resumeX - 10, 
                resumeButton.y + 40 - resumeLayout.height - 5,
                resumeLayout.width + 20,
                resumeLayout.height + 10
            );
            
            if (resumeHitbox.contains(worldX, worldY)) {
                return "resume";
            }
            
            // Check QUIT button
            GlyphLayout quitLayout = new GlyphLayout(buttonFont, "QUIT");
            float quitX = (OVERLAY_WIDTH - quitLayout.width) / 2f;
            
            Rectangle quitHitbox = new Rectangle(
                quitX - 10,
                quitButton.y + 40 - quitLayout.height - 5,
                quitLayout.width + 20,
                quitLayout.height + 10
            );
            
            if (quitHitbox.contains(worldX, worldY)) {
                showConfirmation = true;
                return "confirm";
            }
        } else {
            // Check YES button
            GlyphLayout yesLayout = new GlyphLayout(buttonFont, "YES");
            float yesX = yesButton.x + (BUTTON_WIDTH - yesLayout.width) / 2f;
            
            Rectangle yesHitbox = new Rectangle(
                yesX - 10, 
                yesButton.y + 40 - yesLayout.height - 5,
                yesLayout.width + 20,
                yesLayout.height + 10
            );
            
            if (yesHitbox.contains(worldX, worldY)) {
                showConfirmation = false; // Reset for next time
                return "quit";
            }
            
            // Check NO button
            GlyphLayout noLayout = new GlyphLayout(buttonFont, "NO");
            float noX = noButton.x + (BUTTON_WIDTH - noLayout.width) / 2f;
            
            Rectangle noHitbox = new Rectangle(
                noX - 10,
                noButton.y + 40 - noLayout.height - 5,
                noLayout.width + 20,
                noLayout.height + 10
            );
            
            if (noHitbox.contains(worldX, worldY)) {
                showConfirmation = false; // Back to pause menu
                return "cancel";
            }
        }
        
        return null;
    }
    
    /**
     * Reset confirmation state
     */
    public void resetConfirmation() {
        showConfirmation = false;
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
        buttonFont.dispose();
        confirmFont.dispose();
    }
}
