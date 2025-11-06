package com.main;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.main.map.WarMap;

import com.main.entities.player.Hero;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Texture image;
    private Main game;
    private Hero hero;
    private WarMap map;
    private OrthographicCamera camera;
    private Viewport viewport;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(800,600, camera);
        
        // ✅ Créer le héros au centre de l'écran
        hero = new Hero(400, 300);
        map = new WarMap();
    }

    @Override
    public void show() {
        // Appelé quand l'écran devient actif
    }

    @Override
    public void render(float delta) {
        // Logique de jeu
        update(delta);
        
        // Affichage
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        camera.update();
        map.setView(camera);
        map.render();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        // ✅ Dessiner le héros au lieu de l'image fixe
        hero.render(batch);
        
        batch.end();
    }

    private void update(float delta) {
        // ✅ Mettre à jour le héros avec les limites de la map
        hero.update(delta, map.getMapWidthInPixels(), map.getMapHeightInPixels());
        
        // Ici tu peux ajouter d'autres logiques :
        // - Ennemis
        // - Collision detection
        // - Game logic
        camera.position.set(hero.getPosX(), hero.getPosY(), 0);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        image.dispose();
        map.dispose();
    }
}
