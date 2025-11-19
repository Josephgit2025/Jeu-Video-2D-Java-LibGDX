package com.main;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.ui.BaseDestroyedOverlay;
import com.ui.BaseZombieDestroyedOverlay;
import com.ui.GameOverOverlay;
import com.ui.Inventory;
import com.ui.PauseOverlay;
import com.ui.UnitShop;
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
    private Inventory inventory;

    // Audio
    private Music backgroundMusic;
    private Sound shootSound;

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

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer(); // Pour debug
        camera = new OrthographicCamera();
        // Viewport plus petit pour zoom sur le héros (600x450 au lieu de 800x600)
        viewport = new FitViewport(600, 450, camera);

        viewport.update(com.badlogic.gdx.Gdx.graphics.getWidth(), com.badlogic.gdx.Gdx.graphics.getHeight(), true);

        map = new WarMap();
        this.mapWidth = map.getMapWidthInPixels();
        this.mapHeight = map.getMapHeightInPixels();
        this.enemyBase = new Base(20, 300, false, this.mapHeight); // false = spawn zombies
        this.playerBase = new Base(0, 300, true, this.mapHeight); // true = spawn soldiers
        hero = new Hero(map.getMapWidthInPixels() / 2, map.getMapHeightInPixels() / 2, this.map, this.playerBase);
        this.playerBase.setHero(hero);
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
        this.unitShop = new UnitShop(playerBase, hero, hudDisplay.getGoldDisplay());
        this.unitShop = new UnitShop(playerBase, hero);
        this.inventory = new Inventory(hero);

        // Load audio
        loadSounds();
    }

    /**
     * Charge les sons et la musique du jeu
     */
    private void loadSounds() {
        try {
            System.out.println("🎵 CHARGEMENT DES SONS...");

            // Musique de fond
            backgroundMusic = com.badlogic.gdx.Gdx.audio
                    .newMusic(com.badlogic.gdx.Gdx.files.internal("sounds/debut.mp3"));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.4f);
            backgroundMusic.play();
            System.out.println("✅ Musique de fond chargée et démarrée");

            // Son de tir
            shootSound = com.badlogic.gdx.Gdx.audio
                    .newSound(com.badlogic.gdx.Gdx.files.internal("sounds/coup de feu heros.mp3"));
            System.out.println("✅ Son de tir chargé: " + (shootSound != null ? "OK" : "NULL"));

            // Passer le son au héros
            hero.setShootSound(shootSound);
            System.out.println("✅ Son assigné au héros");

            // TEST: Jouer le son une fois au démarrage pour vérifier
            System.out.println("🔊 TEST: Lecture du son de tir au démarrage...");
            shootSound.play(1.0f);

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des sons : " + e.getMessage());
            System.out.println("Assurez-vous d'avoir les fichiers :");
            System.out.println("  - assets/sounds/debut.mp3");
            System.out.println("  - assets/sounds/coup de feu heros.mp3");
            e.printStackTrace();
        }
    }

    // Recommencer le jeu après avoir perdu
    public void reset() {
        this.map = new WarMap();
        this.enemyBase = new Base(1350, 300, false, this.mapHeight); // false = spawn zombies
        this.playerBase = new Base(-22, 300, true, this.mapHeight); // true = spawn soldiers
        this.hero = new Hero(map.getMapWidthInPixels() / 2, map.getMapHeightInPixels() / 2, this.map, this.playerBase);
        this.playerBase.setHero(hero);
        this.unitShop = new UnitShop(playerBase, hero, hudDisplay.getGoldDisplay());
        this.inventory = new Inventory(hero);

        // ✅ IMPORTANT: Réassigner le son au nouveau héros
        System.out.println("🔄 RESET: Réassignation du son au nouveau héros...");
        if (shootSound != null) {
            hero.setShootSound(shootSound);
            System.out.println("✅ Son réassigné après reset");
        } else {
            System.out.println("❌ ERREUR: shootSound est NULL dans reset()!");
        }

        // Resize the new unitShop to match current window size
        this.unitShop.resize(com.badlogic.gdx.Gdx.graphics.getWidth(), com.badlogic.gdx.Gdx.graphics.getHeight());
        this.inventory.resize(com.badlogic.gdx.Gdx.graphics.getWidth(), com.badlogic.gdx.Gdx.graphics.getHeight());
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
        for (Unit elem : enemyBase.getUnits()) {
            elem.render(batch);
        }
        for (Unit elem : playerBase.getUnits()) {
            elem.render(batch);
        }
        hero.render(batch);
        batch.end();

        // Render Unit Shop buttons
        unitShop.render(shapeRenderer, batch);
        inventory.render(shapeRenderer, batch);

        // Render HUD (after game rendering)
        hudDisplay.update(hero.getCurrentHealth(), hero.getMaxHealth(), hero.getGold());
        hudDisplay.updateBaseHealth(playerBase.getHealth(), 1000, enemyBase.getHealth(), 1000);

        // Mettre à jour les positions des barres de vie des bases
        hudDisplay.updateBaseHealthBarPositions(
                playerBase.getPosX(), playerBase.getPosition().getPosY(),
                enemyBase.getPosX(), enemyBase.getPosition().getPosY(),
                camera);

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
                    shapeRenderer.circle(unit.getPosX() + unit.getWidth() / 2,
                            unit.getPosY() + unit.getHeight() / 2,
                            unit.getRange());
                }
            }

            // Draw enemy units ranges in red
            shapeRenderer.setColor(1, 0, 0, 0.5f); // Red with transparency
            for (Unit unit : enemyBase.getUnits()) {
                if (!unit.isDead()) {
                    shapeRenderer.circle(unit.getPosX() + unit.getWidth() / 2,
                            unit.getPosY() + unit.getHeight() / 2,
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
         * shapeRenderer.setProjectionMatrix(camera.combined);
         * shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
         * shapeRenderer.setColor(1, 0, 0, 1); // Rouge
         * for (Rectangle rect : map.getCollisionRects()) {
         * shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
         * }
         * 
         * // DEBUG: Dessiner le rectangle du héro en vert
         * shapeRenderer.setColor(0, 1, 0, 1); // Vert
         * shapeRenderer.rect(hero.getPosX(), hero.getPosY(), hero.getWidth(),
         * hero.getHeight());
         * 
         * shapeRenderer.end();
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
                    com.badlogic.gdx.Gdx.input.getY());
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
                    com.badlogic.gdx.Gdx.input.getY());

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
                    com.badlogic.gdx.Gdx.input.getY());

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
                    com.badlogic.gdx.Gdx.input.getY());

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
                    com.badlogic.gdx.Gdx.input.getY());

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

        // Remove dead enemies and give gold to hero
        removeDeadEnemiesAndGiveGold();

        hero.update(delta, map.getMapWidthInPixels(), map.getMapHeightInPixels(), enemyBase.getUnits());

        // Spawn des ennemis
        enemyBase.spawnUnit(this, delta);

        // Spawn des alliés
        playerBase.spawnUnit(this, delta);

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
     * Remove dead enemies from the list and give gold to hero
     */
    private void removeDeadEnemiesAndGiveGold() {
        for (Unit enemy : enemyBase.getUnits()) {
            if (enemy.isDead()) {
                // Give gold to hero when enemy dies
                int goldReward = 10;
                hero.addGold(goldReward);
                // System.out.println("Enemy killed! +10 gold. Total: " + hero.getGold());
            }
        }
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
            if (unitShop != null) {
                unitShop.resize(width, height);
            }
            if (inventory != null) {
                inventory.resize(width, height);
            }
        }
    }

    @Override
    public void pause() {
        // Jeu en pause
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    @Override
    public void resume() {
        // Reprendre le jeu
        if (backgroundMusic != null && !backgroundMusic.isPlaying() && gameState == GameState.PLAYING) {
            backgroundMusic.play();
        }
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
        if (inventory != null)
            inventory.dispose();
        if (pauseFont != null)
            pauseFont.dispose();

        // Dispose audio resources
        if (backgroundMusic != null)
            backgroundMusic.dispose();
        if (shootSound != null)
            shootSound.dispose();
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