package com.main;

import com.badlogic.gdx.Game;
import com.main.screens.TitleScreen;

/**
 * Main entry point for the game application.
 * <p>
 * Manages screen transitions and lifecycle for the game, including the main game screen and title screen.
 * Responsible for initializing, switching, and disposing screens.
 */
public class Main extends Game {
    /**
     * The main game screen, responsible for gameplay.
     */
    private GameScreen gameScreen;
    /**
     * The title screen, shown at startup and when returning to menu.
     */
    private TitleScreen titleScreen;

    /**
     * Initializes the game, creating and setting the title and game screens.
     * Called once at application startup.
     */
    @Override
    public void create() {
        gameScreen = new GameScreen(this);
        titleScreen = new TitleScreen(this);
        setScreen(titleScreen);
    }

    /**
     * Shows the title screen, switching from any other screen.
     */
    public void showTitleScreen() {
        setScreen(titleScreen);
    }

    /**
     * Shows the main game screen, resetting the game state before display.
     */
    public void showGameScreen() {
        gameScreen.reset();
        setScreen(gameScreen);
    }

    /**
     * Disposes of all resources used by the game, including screens.
     * Called when the application is closing.
     */
    @Override
    public void dispose() {
        super.dispose();
        if (gameScreen != null) {
            gameScreen.dispose();
        }
    }
}
