package com.main;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.ui.GameOverOverlay;
import com.ui.BaseDestroyedOverlay;
import com.ui.BaseZombieDestroyedOverlay;
import com.ui.PauseOverlay;
import com.ui.UnitShop;

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
    private GameOverOverlay gameOverOverlay;
    private BaseDestroyedOverlay baseDestroyedOverlay;
    private BaseZombieDestroyedOverlay baseZombieDestroyedOverlay;
    private PauseOverlay pauseOverlay;
    private boolean showRanges = false; // Toggle with 'R' key to show unit ranges
    private UnitShop unitShop;
    
    // Pause display (deprecated - using PauseOverlay now)
    private BitmapFont pauseFont;

    // Game Over state
    private enum GameState {
        PLAYING,
        PAUSE,
        GAME_OVER,
        BASE_DESTROYED,
        ZOMBIE_BASE_DESTROYED
    }
    private GameState gameState = GameState.PLAYING;
    private boolean pauseKeyPressed = false;

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
        // Initialize Game Over Overlay
        this.gameOverOverlay = new GameOverOverlay();
        // Initialize Base Destroyed Overlay
        this.baseDestroyedOverlay = new BaseDestroyedOverlay();
        // Initialize Zombie Base Destroyed Overlay
        this.baseZombieDestroyedOverlay = new BaseZombieDestroyedOverlay();
        // Initialize Pause Overlay
        this.pauseOverlay = new PauseOverlay();
        // Initialize Pause Font (deprecated - using PauseOverlay now)
        this.pauseFont = new BitmapFont();
        this.pauseFont.setColor(Color.WHITE);
        this.pauseFont.getData().setScale(4f);
        // Initialize Unit Shop
        this.unitShop = new UnitShop(playerBase, hero);
    }

    // Recommencer le jeu après avoir perdu
    public void reset() {
        // Reset game state
        this.map = new WarMap();
        this.hero = new Hero(map.getMapWidthInPixels() / 2, map.getMapHeightInPixels() / 2, this.map);
        this.enemyBase = new Base(this.mapWidth, 300, false, this.mapHeight); // false = spawn zombies
        this.playerBase = new Base(0, 300, true, this.mapHeight); // true = spawn soldiers
        this.unitShop = new UnitShop(playerBase, hero);
        // Resize the new unitShop to match current window size
        this.unitShop.resize(com.badlogic.gdx.Gdx.graphics.getWidth(), com.badlogic.gdx.Gdx.graphics.getHeight());
        this.gameState = GameState.PLAYING;
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
        
        // Render Unit Shop buttons
        unitShop.render(shapeRenderer, batch);
        
        // Render HUD (after game rendering)
        hudDisplay.update(hero.getCurrentHealth(), hero.getMaxHealth(), hero.getGold());
        hudDisplay.updateBaseHealth(playerBase.getHealth(), 1000, enemyBase.getHealth(), 1000);
        
        // Mettre à jour les positions des barres de vie des bases
        hudDisplay.updateBaseHealthBarPositions(
            playerBase.getPosX(), playerBase.getPosition().getPosY(),
            enemyBase.getPosX(), enemyBase.getPosition().getPosY(),
            camera
        );
        
        // Render base health bars in game world (with game camera)
        hudDisplay.renderBaseHealthBars(camera);
        
        hudDisplay.render();
        
        // Render Pause overlay if in Pause state
        if (gameState == GameState.PAUSE) {
            pauseOverlay.render();
        }
        
        // Render Game Over Overlay if in Game Over state
        if (gameState == GameState.GAME_OVER) {
            gameOverOverlay.render();
        }
        
        // Render Base Destroyed Overlay if player base is destroyed
        if (gameState == GameState.BASE_DESTROYED) {
            baseDestroyedOverlay.render();
        }
        
        // Render Zombie Base Destroyed Overlay if enemy base is destroyed (Victory!)
        if (gameState == GameState.ZOMBIE_BASE_DESTROYED) {
            baseZombieDestroyedOverlay.render();
        }
        
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
        // Toggle pause with ESCAPE key
        if (com.badlogic.gdx.Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            if (!pauseKeyPressed) {
                pauseKeyPressed = true;
                if (gameState == GameState.PLAYING) {
                    gameState = GameState.PAUSE;
                    System.out.println("Game PAUSED");
                } else if (gameState == GameState.PAUSE) {
                    gameState = GameState.PLAYING;
                    System.out.println("Game RESUMED");
                }
            }
        } else {
            pauseKeyPressed = false;
        }
        
        // Handle unit shop clicks (only when playing)
        if (gameState == GameState.PLAYING && com.badlogic.gdx.Gdx.input.justTouched()) {
            unitShop.handleClick(
                com.badlogic.gdx.Gdx.input.getX(),
                com.badlogic.gdx.Gdx.input.getY()
            );
        }
        
        // Check if hero is dead
        if (hero.getCurrentHealth() <= 0 && gameState == GameState.PLAYING) {
            gameState = GameState.GAME_OVER;
            System.out.println("GAME OVER - Hero died!");
        }
        
        // Check if player base is destroyed
        if (playerBase.isDestroyed() && gameState == GameState.PLAYING) {
            gameState = GameState.BASE_DESTROYED;
            System.out.println("BASE DESTROYED - Player base has been destroyed!");
        }
        
        // Check if enemy base is destroyed (Victory!)
        if (enemyBase.isDestroyed() && gameState == GameState.PLAYING) {
            gameState = GameState.ZOMBIE_BASE_DESTROYED;
            System.out.println("VICTORY - Enemy base has been destroyed!");
        }
        
        // Handle Game Over clicks
        if (gameState == GameState.GAME_OVER && com.badlogic.gdx.Gdx.input.justTouched()) {
            String action = gameOverOverlay.handleClick(
                com.badlogic.gdx.Gdx.input.getX(), 
                com.badlogic.gdx.Gdx.input.getY()
            );
            
            if ("replay".equals(action)) {
                System.out.println("Replay clicked!");
                reset();
            } else if ("quit".equals(action)) {
                System.out.println("Quit clicked - Returning to Title Screen!");
                com.badlogic.gdx.Gdx.app.postRunnable(() -> game.showTitleScreen());
            }
        }
        
        // Handle Base Destroyed clicks
        if (gameState == GameState.BASE_DESTROYED && com.badlogic.gdx.Gdx.input.justTouched()) {
            String action = baseDestroyedOverlay.handleClick(
                com.badlogic.gdx.Gdx.input.getX(), 
                com.badlogic.gdx.Gdx.input.getY()
            );
            
            if ("replay".equals(action)) {
                System.out.println("Replay clicked from Base Destroyed!");
                reset();
            } else if ("quit".equals(action)) {
                System.out.println("Quit clicked from Base Destroyed - Returning to Title Screen!");
                com.badlogic.gdx.Gdx.app.postRunnable(() -> game.showTitleScreen());
            }
        }
        
        // Handle Zombie Base Destroyed clicks
        if (gameState == GameState.ZOMBIE_BASE_DESTROYED && com.badlogic.gdx.Gdx.input.justTouched()) {
            String action = baseZombieDestroyedOverlay.handleClick(
                com.badlogic.gdx.Gdx.input.getX(), 
                com.badlogic.gdx.Gdx.input.getY()
            );
            
            if ("replay".equals(action)) {
                System.out.println("Replay clicked from Zombie Base Destroyed!");
                reset();
            } else if ("quit".equals(action)) {
                System.out.println("Quit clicked from Zombie Base Destroyed - Returning to Title Screen!");
                com.badlogic.gdx.Gdx.app.postRunnable(() -> game.showTitleScreen());
            }
        }
        
        // Handle Pause clicks
        if (gameState == GameState.PAUSE && com.badlogic.gdx.Gdx.input.justTouched()) {
            String action = pauseOverlay.handleClick(
                com.badlogic.gdx.Gdx.input.getX(), 
                com.badlogic.gdx.Gdx.input.getY()
            );
            
            if ("resume".equals(action)) {
                System.out.println("Resume clicked!");
                gameState = GameState.PLAYING;
                pauseOverlay.resetConfirmation();
            } else if ("quit".equals(action)) {
                System.out.println("Quit to menu confirmed - Returning to Title Screen!");
                pauseOverlay.resetConfirmation();
                com.badlogic.gdx.Gdx.app.postRunnable(() -> game.showTitleScreen());
            } else if ("confirm".equals(action)) {
                System.out.println("Showing quit confirmation...");
            } else if ("cancel".equals(action)) {
                System.out.println("Quit cancelled, back to pause menu");
            }
        }
        
        // Don't update game if Game Over, Base Destroyed, or Paused
        if (gameState == GameState.GAME_OVER || gameState == GameState.PAUSE || 
            gameState == GameState.BASE_DESTROYED || gameState == GameState.ZOMBIE_BASE_DESTROYED) {
            return;
        }
        
        // Toggle range display with 'R' key
        if (com.badlogic.gdx.Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.R)) {
            showRanges = !showRanges;
            System.out.println("Range display: " + (showRanges ? "ON" : "OFF"));
        }

        
        // vérifier collision entre héros et ennemis
        checkHeroEnemyCollisions(delta);
        
        // Remove dead enemies and give gold to hero
        removeDeadEnemiesAndGiveGold();
        
        hero.update(delta, map.getMapWidthInPixels(), map.getMapHeightInPixels(), enemyBase.getUnits());
        
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
            // System.out.println("GAME OVER - Player base destroyed!");
            // TODO: Implement game over screen
        }
        if (enemyBase.isDestroyed()) {
            // System.out.println("VICTORY - Enemy base destroyed!");
            // TODO: Implement victory screen
        }
        
        camera.position.set(hero.getPosX(), hero.getPosY(), 0);
    }
    
    /**
     vérifier collision entre héros et ennemis et appliquer des dégâts sur lui
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
            if (distance < 50f) {
                enemy.setTarget(this.hero);
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
                int goldReward = 10;
                hero.addGold(goldReward);
                // System.out.println("Enemy killed! +10 gold. Total: " + hero.getGold());
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
        if (gameOverOverlay != null) {
            gameOverOverlay.resize(width, height);
        }
        if (baseDestroyedOverlay != null) {
            baseDestroyedOverlay.resize(width, height);
        }
        if (baseZombieDestroyedOverlay != null) {
            baseZombieDestroyedOverlay.resize(width, height);
        }
        if (pauseOverlay != null) {
            pauseOverlay.resize(width, height);
        }
        if (unitShop != null) {
            unitShop.resize(width, height);
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
        if (gameOverOverlay != null)
            gameOverOverlay.dispose();
        if (baseDestroyedOverlay != null)
            baseDestroyedOverlay.dispose();
        if (baseZombieDestroyedOverlay != null)
            baseZombieDestroyedOverlay.dispose();
        if (pauseOverlay != null)
            pauseOverlay.dispose();
        if (pauseFont != null)
            pauseFont.dispose();
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