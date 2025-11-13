package com.main;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private boolean showRanges = false; // Toggle with 'R' key to show unit ranges

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
        this.enemyBase = new Base(this.mapWidth, 300, false, this.mapHeight); // false = spawn zombies
        this.playerBase = new Base(0, 300, true, this.mapHeight); // true = spawn soldiers
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
        // Render toutes les unités
        for (Unit elem : enemyBase.getUnits()){
            elem.render(batch);
        }
        for (Unit elem : playerBase.getUnits()){
            elem.render(batch);
        }
        hero.render(batch);
        batch.end();
        
        // Render HUD (after game rendering)
        hudDisplay.update(hero.getCurrentHealth(), hero.getMaxHealth(), hero.getGold());
        hudDisplay.render();
        
        // Draw range circles if enabled
        if (showRanges) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            
            // Draw player units ranges in green
            shapeRenderer.setColor(0, 1, 0, 0.5f); // Green with transparency
            for (Unit unit : playerBase.getUnits()) {
                if (!unit.isDead()) {
                    shapeRenderer.circle(unit.getPosX() + unit.getWidth()/2, 
                                        unit.getPosY() + unit.getHeight()/2, 
                                        unit.getRange());
                }
            }
            
            // Draw enemy units ranges in red
            shapeRenderer.setColor(1, 0, 0, 0.5f); // Red with transparency
            for (Unit unit : enemyBase.getUnits()) {
                if (!unit.isDead()) {
                    shapeRenderer.circle(unit.getPosX() + unit.getWidth()/2, 
                                        unit.getPosY() + unit.getHeight()/2, 
                                        unit.getRange());
                }
            }
            
            // Draw base collision hitboxes (purple for player, yellow for enemy)
            shapeRenderer.setColor(0.5f, 0, 0.5f, 0.5f); // Purple for player base hitbox
            com.badlogic.gdx.math.Rectangle playerBox = playerBase.getCollisionBox();
            shapeRenderer.rect(playerBox.x, playerBox.y, playerBox.width, playerBox.height);
            
            shapeRenderer.setColor(1, 1, 0, 0.5f); // Yellow for enemy base hitbox
            com.badlogic.gdx.math.Rectangle enemyBox = enemyBase.getCollisionBox();
            shapeRenderer.rect(enemyBox.x, enemyBox.y, enemyBox.width, enemyBox.height);
            
            shapeRenderer.end();
        }
        
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
        // Toggle range display with 'R' key
        if (com.badlogic.gdx.Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.R)) {
            showRanges = !showRanges;
            System.out.println("Range display: " + (showRanges ? "ON" : "OFF"));
        }

        
        // vérifier collision entre héros et ennemis
        checkHeroEnemyCollisions(delta);
        
        // Remove dead enemies and give gold to hero
        removeDeadEnemiesAndGiveGold();
        
        hero.update(delta, map.getMapWidthInPixels(), map.getMapHeightInPixels());
        
        // Spawn des ennemis
        Unit tmpEnemy = enemyBase.spawnUnit(this, delta);
        if (tmpEnemy != null){
            enemyBase.addUnit(tmpEnemy);
        }
        
        // Spawn des alliés
        Unit tmpAlly = playerBase.spawnUnit(this, delta);
        if (tmpAlly != null){
            playerBase.addUnit(tmpAlly);
        }
        
        // Update : les ennemis attaquent les alliés (et leur base) et vice-versa
        enemyBase.updateUnits(delta, playerBase.getUnits(), playerBase, this.hero);
        playerBase.updateUnits(delta, enemyBase.getUnits(), enemyBase, null);
        
        // Check for game over conditions
        if (playerBase.isDestroyed()) {
            System.out.println("GAME OVER - Player base destroyed!");
            // TODO: Implement game over screen
        }
        if (enemyBase.isDestroyed()) {
            System.out.println("VICTORY - Enemy base destroyed!");
            // TODO: Implement victory screen
        }
        
        camera.position.set(hero.getPosX(), hero.getPosY(), 0);
    }
    
    /**
     * vérifier collision entre héros et ennemis et appliquer des dégâts sur lui
     */
    private void checkHeroEnemyCollisions(float delta) {
        for (Unit enemy : enemyBase.getUnits()) {
            if (enemy.isDead()) continue;
            
            // Simple collision detection using bounding boxes
            float heroX = hero.getPosX();
            float heroY = hero.getPosY();
            float enemyX = enemy.getPosX();
            float enemyY = enemy.getPosY();
            
            // Calculate distance between hero and enemy
            float dx = heroX - enemyX;
            float dy = heroY - enemyY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            
            // If enemy is close enough to attack (within range)
            if (distance < 50f) { // 50 pixels = attack range
                // Enemy damages hero (1 damage per second, adjusted by delta time)
                enemy.setTarget(this.hero);
                System.out.println("Hero hit! HP: " + hero.getCurrentHealth() + "/" + hero.getMaxHealth());
            }
        }
    }
    
    /**
     * Remove dead enemies from the list and give gold to hero
     */
    private void removeDeadEnemiesAndGiveGold() {
        List<Unit> enemiesToRemove = new ArrayList<>();
        
        for (Unit enemy : enemyBase.getUnits()) {
            if (enemy.isDead()) {
                // Give gold to hero when enemy dies
                int goldReward = 10; // 10 gold per enemy killed
                hero.addGold(goldReward);
                System.out.println("Enemy killed! +10 gold. Total: " + hero.getGold());
                enemiesToRemove.add(enemy);
            }
        }
        
        // Remove dead enemies
        enemyBase.getUnits().removeAll(enemiesToRemove);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if (hudDisplay != null) {
            hudDisplay.resize(width, height);
        }
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