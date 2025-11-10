package com.main;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.main.entities.Unit;
import com.main.entities.player.Hero;
import com.main.map.Base;
import com.main.map.WarMap;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Texture image;

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

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);

        viewport.update(com.badlogic.gdx.Gdx.graphics.getWidth(), com.badlogic.gdx.Gdx.graphics.getHeight(), true);

        map = new WarMap();
        hero = new Hero(map.getMapWidthInPixels() / 2, map.getMapHeightInPixels() / 2, this.map);
        this.mapWidth = map.getMapWidthInPixels();
        this.mapHeight = map.getMapHeightInPixels();
        this.enemyBase = new Base(this.mapWidth, 300);
        this.playerBase = new Base(0, 300);
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
