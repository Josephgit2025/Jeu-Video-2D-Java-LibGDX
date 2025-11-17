package com.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.main.entities.Unit;
import com.main.entities.player.Hero;
import com.main.map.Base;

public class UnitShop {

    private List<UnitShopButton> unitTypeButtons;
    private List<UnitShopButton> spawnPointButtons;
    private Base playerBase;
    private Hero hero;

    // UI Camera and Viewport (same as HUD)
    private Viewport viewport;
    private OrthographicCamera camera;
    private Vector3 touchPos;

    // Selected spawn point (default to 0)
    private int selectedSpawnPoint = 0;

    private static final float BUTTON_WIDTH = 60f;
    private static final float BUTTON_HEIGHT = 40f;
    private static final float BUTTON_SPACING = 10f;
    private static final float START_X = 540f; // À droite (800 - 3*60 - 2*10 - 20)
    private static final float UNIT_BUTTONS_Y = 520f; // En haut
    private static final float SPAWN_BUTTONS_Y = 470f; // Juste en dessous
    protected List<Texture> loadedTextures = new ArrayList<>();
    // protected TextureRegion idleFrame;

    // UI Viewport dimensions (same as HUD)
    private static final float UI_WIDTH = 800f;
    private static final float UI_HEIGHT = 600f;

    public UnitShop(Base playerBase, Hero hero) {
        this.playerBase = playerBase;
        this.hero = hero;
        this.unitTypeButtons = new ArrayList<>();
        this.spawnPointButtons = new ArrayList<>();

        // Initialize UI camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(UI_WIDTH, UI_HEIGHT, camera);
        camera.position.set(UI_WIDTH / 2, UI_HEIGHT / 2, 0);

        touchPos = new Vector3();

        createButtons();
    }

    private void createButtons() {

        Base.Type[] types = { Base.Type.MELEE, Base.Type.SNIPER, Base.Type.TANK };

        // Créer les 3 boutons de type d'unité (en haut)
        for (int i = 0; i < types.length; i++) {
            float buttonX = START_X + i * (BUTTON_WIDTH + BUTTON_SPACING);
            unitTypeButtons.add(new UnitShopButton(
                    buttonX,
                    UNIT_BUTTONS_Y,
                    BUTTON_WIDTH,
                    BUTTON_HEIGHT,
                    types[i]));
        }

        // Créer les 3 boutons de point de spawn (en bas)
        for (int i = 0; i < 3; i++) {
            float buttonX = START_X + i * (BUTTON_WIDTH + BUTTON_SPACING);
            UnitShopButton spawnBtn = new UnitShopButton(
                    buttonX,
                    SPAWN_BUTTONS_Y,
                    BUTTON_WIDTH,
                    BUTTON_HEIGHT,
                    i);
            // Sélectionner le premier bouton par défaut
            if (i == selectedSpawnPoint) {
                spawnBtn.setSelected(true);
            }
            spawnPointButtons.add(spawnBtn);
        }

    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {

        // Update camera
        camera.update();

        // Set projection matrices to UI camera
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        // Render spawn point buttons
        for (UnitShopButton button : spawnPointButtons) {
            button.render(shapeRenderer, batch);
        }

        // Render unit type buttons
        for (UnitShopButton button : unitTypeButtons) {
            button.render(shapeRenderer, batch);
        }

    }

    public boolean handleClick(int screenX, int screenY) {
        // Convert screen coordinates to UI viewport coordinates
        touchPos.set(screenX, screenY, 0);
        viewport.unproject(touchPos);

        // Check spawn point buttons first
        for (int i = 0; i < spawnPointButtons.size(); i++) {
            UnitShopButton button = spawnPointButtons.get(i);
            if (button.isClicked(touchPos.x, touchPos.y)) {
                // Deselect all spawn buttons
                for (UnitShopButton btn : spawnPointButtons) {
                    btn.setSelected(false);
                }
                // Select clicked button
                button.setSelected(true);
                selectedSpawnPoint = i;
                return true;
            }
        }

        // Check unit type buttons
        for (UnitShopButton button : unitTypeButtons) {
            if (button.isClicked(touchPos.x, touchPos.y)) {
                // Buy unit with selected spawn point
                Unit newUnit = playerBase.buyUnit(button.getUnitType(), selectedSpawnPoint, hero);
                if (newUnit != null) {
                    playerBase.addUnit(newUnit);
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Resize the viewport when window size changes
     */
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(UI_WIDTH / 2, UI_HEIGHT / 2, 0);
    }
}
