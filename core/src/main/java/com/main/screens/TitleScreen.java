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
import com.main.GameScreen;
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
            float logoWidth = 650f;
            float logoHeight = 220f;
            float logoX = (WORLD_WIDTH - logoWidth) / 2f;
            float logoY = WORLD_HEIGHT - logoHeight - 80f;
            batch.draw(titleLogo, logoX, logoY, logoWidth, logoHeight);
        }

        // ==== MENU ====
        if (font != null) {
            font.getData().setScale(1.1f);
            float startY = WORLD_HEIGHT / 2f - 120f; // position sous le logo
            float spacing = 55f;

            for (int i = 0; i < menuItems.length; i++) {
                String item = menuItems[i];
                Color color = (i == selectedIndex) ? Color.YELLOW : Color.WHITE;
                drawCenteredTextShadow(item, startY - i * spacing, color);
            }
        }

        batch.end();
    }

    // Texte centré avec ombre douce
    private void drawCenteredTextShadow(String text, float y, Color color) {
        if (font == null) return;
        
        GlyphLayout layout = new GlyphLayout(font, text);
        float x = (WORLD_WIDTH - layout.width) / 2f;

        // Ombre
        font.setColor(0f, 0f, 0f, 0.6f);
        font.draw(batch, layout, x + 3, y - 3);

        // Couleur principale
        font.setColor(color);
        font.draw(batch, layout, x, y);
    }

    private void updateHover() {
        if (font == null) return;
        
        Vector2 mouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        float mx = mouse.x;
        float my = mouse.y;

        selectedIndex = -1;
        float startY = WORLD_HEIGHT / 2f - 120f;
        float spacing = 55f;

        for (int i = 0; i < menuItems.length; i++) {
            float textY = startY - i * spacing;
            GlyphLayout layout = new GlyphLayout(font, menuItems[i]);
            float textX = (WORLD_WIDTH - layout.width) / 2f;
            float textHeight = layout.height;

            // Créer un rectangle de collision avec des marges
            com.badlogic.gdx.math.Rectangle buttonRect = new com.badlogic.gdx.math.Rectangle(
                textX - 10,
                textY - textHeight - 5,
                layout.width + 20,
                textHeight + 10
            );
            
            if (buttonRect.contains(mx, my)) {
                selectedIndex = i;
            }
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