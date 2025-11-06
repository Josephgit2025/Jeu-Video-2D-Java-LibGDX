package com.main;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.main.map.Base;
import com.main.map.WarMap;
import com.main.entities.Unit;
import com.main.entities.player.Hero;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Texture image;
    
    private Main game;
    private Hero hero;
    private WarMap map;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Base enemyBase;
    private Base playerBase;
    private int mapWidth;
    private int mapHeight;
    
    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1000,700, camera);
        
        // ✅ Créer le héros au centre de l'écran
        map = new WarMap();
        hero = new Hero(map.getMapWidthInPixels() / 2, map.getMapHeightInPixels() / 2);
        this.mapWidth = map.getMapWidthInPixels();
        this.mapHeight = map.getMapHeightInPixels();
        this.enemyBase = new Base(this.mapWidth, 300);
        this.playerBase = new Base(0, 300);
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
        for (Unit elem : playerBase.getUnits()){
            elem.render(batch);
        }
        batch.end();
    }

    private void update(float delta) {
        // ✅ Mettre à jour le héros avec les limites de la map
        hero.update(delta, map.getMapWidthInPixels(), map.getMapHeightInPixels());
        
        // Ici tu peux ajouter d'autres logiques :
        // - Ennemis
        // - Collision detection
        // - Game logic
        Unit tmp = playerBase.spawnUnit(this, delta);
        if (tmp != null){
            playerBase.addUnit(tmp);;
        }
        playerBase.updateUnits(delta);
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
