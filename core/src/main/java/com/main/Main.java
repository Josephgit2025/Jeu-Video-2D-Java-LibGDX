package com.main;

import com.badlogic.gdx.Game;
import com.main.screens.TitleScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
// Main gere les ecrans, GameScreen gere le jeu
public class Main extends Game {
    private GameScreen gameScreen;

    @Override
    public void create() {
        // Créer et définir l'écran de jeu
        gameScreen = new GameScreen(this);
        setScreen(new TitleScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (gameScreen != null) {
            gameScreen.dispose();
        }
    }
}
