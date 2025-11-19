package com.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.main.entities.player.Hero;
import com.main.weapons.Weapon;
import com.ui.UnitShopButton.ButtonType;

public class Inventory {
    private List<UnitShopButton> unitTypeButtons;
    private List<UnitShopButton> spawnPointButtons;
    private Hero hero;
    private String weapon;
    private String currentWeaponName;

    private Viewport viewport;
    private OrthographicCamera camera;
    private Vector3 touchPos;

    private static final float BUTTON_WIDTH = 60f;
    private static final float BUTTON_HEIGHT = 60f;
    private static final float START_X = 640f; // À droite (800 - 3*60 - 2*10 - 20)
    private static final float UNIT_BUTTONS_Y = 20f; // En haut
    protected List<Texture> loadedTextures = new ArrayList<>();

    private static final float UI_WIDTH = 800f;
    private static final float UI_HEIGHT = 600f; 
    protected TextureRegion idleFrame;
    private Rectangle bounds;

    public Inventory(Hero hero) {
        this.hero = hero;
        this.weapon = hero.getWeapon().getClass().getSimpleName();
        this.currentWeaponName = this.weapon;
        System.out.println(weapon);
        this.unitTypeButtons = new ArrayList<>();
        this.spawnPointButtons = new ArrayList<>();

        // Initialize UI camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(UI_WIDTH, UI_HEIGHT, camera);
        camera.position.set(UI_WIDTH / 2, UI_HEIGHT / 2, 0);

        touchPos = new Vector3();

        Texture idleTex = new Texture(Gdx.files.internal("weapon/" + this.weapon + ".png"));
        this.idleFrame = new TextureRegion(idleTex);
        // START_X is already in pixels; do not multiply by BUTTON_WIDTH again.
        float buttonX = START_X;

        // Button area: use BUTTON_WIDTH x BUTTON_HEIGHT (not full UI height)
        this.bounds = new Rectangle(buttonX, UNIT_BUTTONS_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        // register the texture for disposal if needed
        loadedTextures.add(idleTex);
    }

    private void reloadWeaponTexture(String weaponName) {
        // dispose previous textures
        for (Texture t : loadedTextures) {
            try {
                t.dispose();
            } catch (Exception e) {
                // ignore
            }
        }
        loadedTextures.clear();

        // load new texture
        try {
            Texture idleTex = new Texture(Gdx.files.internal("weapon/" + weaponName + ".png"));
            this.idleFrame = new TextureRegion(idleTex);
            loadedTextures.add(idleTex);
            this.weapon = weaponName;
            this.currentWeaponName = weaponName;
            System.out.println("Inventory: weapon texture reloaded -> " + weaponName);
        } catch (Exception e) {
            System.out.println("Inventory: failed to load weapon texture for " + weaponName + " : " + e.getMessage());
        }
    }

    // public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {

    //     // Update camera
    //     camera.update();

    //     // Set projection matrices to UI camera
    //     shapeRenderer.setProjectionMatrix(camera.combined);
    //     batch.setProjectionMatrix(camera.combined);

    //     // Render spawn point buttons
    //     for (UnitShopButton button : spawnPointButtons) {
    //         button.render(shapeRenderer, batch);
    //     }

    //     // Render unit type buttons
    //     for (UnitShopButton button : unitTypeButtons) {
    //         button.render(shapeRenderer, batch);
    //     }

    // }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        // Ensure UI camera/projection is used for UI rendering
        camera.update();
        viewport.apply();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        // Check whether hero weapon changed and reload texture if needed
        String heroWeaponName = hero.getWeapon().getClass().getSimpleName();
        if (currentWeaponName == null || !currentWeaponName.equals(heroWeaponName)) {
            reloadWeaponTexture(heroWeaponName);
        }

        TextureRegion currentFrame = idleFrame;

        float imgW = currentFrame.getRegionWidth();
        float imgH = currentFrame.getRegionHeight();

        float scale = (float) Math.min(bounds.width / imgW * 1.5, bounds.height / imgH * 1.5); // garder ratio

        float drawW = imgW * scale;
        float drawH = imgH * scale;

        float drawX = bounds.x + (bounds.width - drawW) / 2;
        float drawY = bounds.y + (bounds.height - drawH) / 2;

        // Dessiner le rectangle du bouton
        // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // // shapeRenderer.setColor(color);
        // shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        // shapeRenderer.end();

        // Bordure (plus épaisse si sélectionné)
        // shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //     shapeRenderer.setColor(Color.PURPLE);
        //     shapeRenderer.rect(drawX + 16, drawY, drawW - 37, drawH + 1);

        // // else {
        // // shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        // // }
        // shapeRenderer.end();

        // Dessiner le label
        batch.begin();
            batch.draw(currentFrame, drawX, drawY, drawW, drawH);
        batch.end();
    }

    public void dispose() {
        for (Texture t : loadedTextures) {
            try {
                t.dispose();
            } catch (Exception e) {
                // ignore
            }
        }
        loadedTextures.clear();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(UI_WIDTH / 2, UI_HEIGHT / 2, 0);
    }

}
