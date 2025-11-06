package com.main;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Texture image;
    private Main game;
    private Hero hero; // ✅ Ajouter le héros

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        
        // ✅ Créer le héros au centre de l'écran
        hero = new Hero(400, 300);
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
        batch.begin();
        
        // ✅ Dessiner le héros au lieu de l'image fixe
        hero.render(batch);
        
        batch.end();
    }

    private void update(float delta) {
        // ✅ Mettre à jour le héros
        hero.update(delta);
        
        // Ici tu peux ajouter d'autres logiques :
        // - Ennemis
        // - Collision detection
        // - Game logic
    }

    @Override
    public void resize(int width, int height) {
        // Redimensionnement de la fenêtre
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
        hero.dispose(); // ✅ Nettoyer les ressources du héros
    }
}
