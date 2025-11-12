package com.main;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.main.entities.Unit;
import com.main.entities.player.Hero;
import com.main.map.Base;
import com.main.map.WarMap;
import com.ui.hud;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Texture image;
    private ShapeRenderer shapeRenderer; // Pour debug des collisions

    private Main game;
    private Hero hero;
    private WarMap map;
    private OrthographicCamera camera;
    private Viewport viewport;
    private boolean moving;
    private Direction direction;
    private Base enemyBase;
    private Base playerBase;
    private int mapWidth;
    private int mapHeight;
    private hud hudDisplay;

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer(); // Pour debug
        camera = new OrthographicCamera();
        // Viewport plus petit pour zoom sur le héros (600x450 au lieu de 800x600)
        viewport = new FitViewport(600, 450, camera);

        viewport.update(com.badlogic.gdx.Gdx.graphics.getWidth(), com.badlogic.gdx.Gdx.graphics.getHeight(), true);

        map = new WarMap();
        hero = new Hero(map.getMapWidthInPixels() / 2, map.getMapHeightInPixels() / 2, this.map);
        this.mapWidth = map.getMapWidthInPixels();
        this.mapHeight = map.getMapHeightInPixels();
        this.enemyBase = new Base(this.mapWidth, 300);
        this.playerBase = new Base(0, 300);
        
        // Initialize HUD
        this.hudDisplay = new hud();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        float spriteHalfWidth = hero.getSprite().getWidth() / 2f;
        float spriteHalfHeight = hero.getSprite().getHeight() / 2f;

        float camX = hero.getPosX() + spriteHalfWidth;
        float camY = hero.getPosY() + spriteHalfHeight;

        float halfViewportWidth = viewport.getWorldWidth() / 2f;
        float halfViewportHeight = viewport.getWorldHeight() / 2f;

        float mapPxW = map.getMapWidthInPixels();
        float mapPxH = map.getMapHeightInPixels();

        if (mapPxW > viewport.getWorldWidth()) {
            camX = Math.max(halfViewportWidth, Math.min(camX, mapPxW - halfViewportWidth));
        } else {
            camX = mapPxW / 2f;
        }

        if (mapPxH > viewport.getWorldHeight()) {
            camY = Math.max(halfViewportHeight, Math.min(camY, mapPxH - halfViewportHeight));
        } else {
            camY = mapPxH / 2f;
        }

        camera.position.set(camX, camY, 0);
        camera.update();
        map.setView(camera);
        map.render();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Unit elem : enemyBase.getUnits()){
            elem.render(batch);
        }
        hero.render(batch);
        batch.end();
        
        // Render HUD (after game rendering)
        hudDisplay.update(hero.getCurrentHealth(), hero.getMaxHealth(), hero.getGold());
        hudDisplay.render();
        
        // DEBUG: Dessiner les rectangles de collision (décommenter pour debug)
        /*
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1); // Rouge
        for (Rectangle rect : map.getCollisionRects()) {
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        
        // DEBUG: Dessiner le rectangle du héro en vert
        shapeRenderer.setColor(0, 1, 0, 1); // Vert
        shapeRenderer.rect(hero.getPosX(), hero.getPosY(), hero.getWidth(), hero.getHeight());
        
        shapeRenderer.end();
        */
    }

    private void update(float delta) {
        hero.update(delta, map.getMapWidthInPixels(), map.getMapHeightInPixels());
        Unit tmp = enemyBase.spawnUnit(this, delta);
        if (tmp != null){
            enemyBase.addUnit(tmp);
        }
        enemyBase.updateUnits(delta);
        camera.position.set(hero.getPosX(), hero.getPosY(), 0);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudDisplay.resize(width, height);
    }

    @Override
    public void pause() {
        // Jeu en pause
    }

    @Override
    public void resume() {
        // Reprendre le jeu
    }

    @Override
    public void hide() {
        // Écran caché
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (image != null)
            image.dispose();
        if (hero != null)
            hero.dispose();
        if (map != null)
            map.dispose();
        if (hudDisplay != null)
            hudDisplay.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Texture getImage() {
        return image;
    }

    public Main getGame() {
        return game;
    }

    public Hero getHero() {
        return hero;
    }

    public WarMap getMap() {
        return map;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Base getEnemyBase() {
        return enemyBase;
    }

    public Base getPlayerBase() {
        return playerBase;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }
}
