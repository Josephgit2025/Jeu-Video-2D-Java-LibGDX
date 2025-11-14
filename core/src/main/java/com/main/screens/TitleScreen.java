package com.main.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.main.Main;

public class TitleScreen implements Screen {

    protected Main game;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected BitmapFont font;
    protected Texture background;
    protected Texture titleLogo;      // logo ajouté

    private static final float WORLD_WIDTH = 800f;
    private static final float WORLD_HEIGHT = 480f;

    private String[] menuItems = {"PLAY", "QUIT"};
    protected int selectedIndex = -1;

    public TitleScreen(Main game) {
        this.game = game;
        
        try {
            batch = new SpriteBatch();
        } catch (Exception e) {
            System.err.println("Warning: Could not create SpriteBatch (headless mode?)");
            batch = null;
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        // Image de fond
        try {
            background = new Texture(Gdx.files.internal("ui/titlescreen.png"));
        } catch (Exception e) {
            System.err.println("Warning: Could not load background texture (headless mode?)");
            background = null;
        }

        // Logo principal
        try {
            titleLogo = new Texture(Gdx.files.internal("ui/titlelogo.png"));
        } catch (Exception e) {
            System.err.println("Warning: Could not load title logo texture (headless mode?)");
            titleLogo = null;
        }

        // === Police rétro pixel pour le menu
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 32;
            parameter.borderWidth = 2.5f;
            parameter.borderColor = new Color(0f, 0f, 0f, 0.85f);
            parameter.shadowOffsetX = 2;
            parameter.shadowOffsetY = -2;
            parameter.shadowColor = new Color(0f, 0f, 0f, 0.7f);
            parameter.magFilter = Texture.TextureFilter.Nearest;
            parameter.minFilter = Texture.TextureFilter.Nearest;
            font = generator.generateFont(parameter);
            generator.dispose();
        } catch (Exception e) {
            System.err.println("Warning: Could not load font (headless mode?)");
            font = new BitmapFont(); // Fallback to default font
        }
    }

    @Override
    public void render(float delta) {
        if (batch == null) return; // Skip rendering in headless mode
        
        Gdx.gl.glClearColor(0f, 0f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        updateHover();
        handleInput();

        batch.begin();

        // Fond
        if (background != null) {
            batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        }

        // LOGO PRINCIPAL
        if (titleLogo != null) {
            float logoWidth = 500f;  // Agrandi encore plus
            float logoHeight = 500f; // Carré pour respecter les proportions originales
            float logoX = (WORLD_WIDTH - logoWidth) / 2f;
            float logoY = WORLD_HEIGHT - logoHeight + 30f; // Remonté plus haut
            batch.draw(titleLogo, logoX, logoY, logoWidth, logoHeight);
        }

        // ==== MENU - BOUTONS EN LIGNE ====
        if (font != null) {
            font.getData().setScale(0.8f); // Taille réduite des boutons
            
            // Position Y avec espace entre le logo et les boutons
            float buttonY = 60f; // Position descendue
            
            // Calculer les layouts pour centrer les deux boutons
            GlyphLayout playLayout = new GlyphLayout(font, "PLAY");
            GlyphLayout quitLayout = new GlyphLayout(font, "QUIT");
            
            // Espacement entre les boutons
            float buttonSpacing = 80f;
            
            // Largeur totale occupée par les deux boutons
            float totalWidth = playLayout.width + buttonSpacing + quitLayout.width;
            
            // Position de départ pour centrer l'ensemble
            float startX = (WORLD_WIDTH - totalWidth) / 2f;
            
            // Dessiner PLAY avec effet d'inversion
            float playX = startX;
            if (selectedIndex == 0) {
                drawTextWithShadowInverted("PLAY", playX, buttonY);
            } else {
                drawTextWithShadow("PLAY", playX, buttonY, Color.WHITE);
            }
            
            // Dessiner QUIT avec effet d'inversion
            float quitX = startX + playLayout.width + buttonSpacing;
            if (selectedIndex == 1) {
                drawTextWithShadowInverted("QUIT", quitX, buttonY);
            } else {
                drawTextWithShadow("QUIT", quitX, buttonY, Color.WHITE);
            }
        }

        batch.end();
    }

    // Texte avec ombre douce (position manuelle)
    private void drawTextWithShadow(String text, float x, float y, Color color) {
        if (font == null) return;
        
        GlyphLayout layout = new GlyphLayout(font, text);

        // Ombre
        font.setColor(0f, 0f, 0f, 0.6f);
        font.draw(batch, layout, x + 3, y - 3);

        // Couleur principale
        font.setColor(color);
        font.draw(batch, layout, x, y);
    }

    // Texte avec effet d'inversion (hover) - fond jaune et texte noir
    private void drawTextWithShadowInverted(String text, float x, float y) {
        if (font == null) return;
        
        GlyphLayout layout = new GlyphLayout(font, text);

        // Ombre jaune/dorée (plus épaisse pour effet de fond)
        font.setColor(Color.YELLOW);
        for (int offsetX = -2; offsetX <= 2; offsetX++) {
            for (int offsetY = -2; offsetY <= 2; offsetY++) {
                font.draw(batch, layout, x + offsetX, y + offsetY);
            }
        }

        // Texte noir par-dessus
        font.setColor(Color.BLACK);
        font.draw(batch, layout, x, y);
    }

    private void updateHover() {
        if (font == null) return;
        
        Vector2 mouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        float mx = mouse.x;
        float my = mouse.y;

        selectedIndex = -1;
        
        // Position Y des boutons (même que dans render)
        float buttonY = 60f;
        
        // Calculer les layouts
        GlyphLayout playLayout = new GlyphLayout(font, "PLAY");
        GlyphLayout quitLayout = new GlyphLayout(font, "QUIT");
        
        float buttonSpacing = 80f;
        float totalWidth = playLayout.width + buttonSpacing + quitLayout.width;
        float startX = (WORLD_WIDTH - totalWidth) / 2f;
        
        // Zone de collision PLAY
        float playX = startX;
        Rectangle playRect = new Rectangle(
            playX - 10,
            buttonY - playLayout.height - 5,
            playLayout.width + 20,
            playLayout.height + 10
        );
        
        if (playRect.contains(mx, my)) {
            selectedIndex = 0;
            return;
        }
        
        // Zone de collision QUIT
        float quitX = startX + playLayout.width + buttonSpacing;
        Rectangle quitRect = new Rectangle(
            quitX - 10,
            buttonY - quitLayout.height - 5,
            quitLayout.width + 20,
            quitLayout.height + 10
        );
        
        if (quitRect.contains(mx, my)) {
            selectedIndex = 1;
        }
    }

    protected void handleInput() {
        if (Gdx.input.justTouched() && selectedIndex != -1) {
            switch (menuItems[selectedIndex]) {
                case "PLAY":
                    game.showGameScreen();
                    break;
                case "QUIT":
                    Gdx.app.postRunnable(() -> {
                        Gdx.app.exit();
                        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
                            System.exit(0);
                    });
                    break;
            }
        }
    }
    
    // Helper method for testing handleInput with specific selectedIndex
    protected void handleInputWithSelection(int selection) {
        int oldSelection = selectedIndex;
        selectedIndex = selection;
        handleInput();
        selectedIndex = oldSelection;
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
        if (background != null) background.dispose();
        if (titleLogo != null) titleLogo.dispose();
    }
}