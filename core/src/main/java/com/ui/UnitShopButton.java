package com.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.main.map.Base;

public class UnitShopButton {

    public enum ButtonType {
        UNIT_TYPE, // Bouton pour sélectionner le type d'unité
        SPAWN_POINT // Bouton pour sélectionner le point de spawn
    }

    private Rectangle bounds;
    private Base.Type unitType;
    private Integer spawnIndex; // Nullable pour les boutons de type d'unité
    private Color color;
    private Color borderColor;
    private ButtonType buttonType;
    private boolean selected;
    private String label;
    private BitmapFont font;
    protected List<Texture> loadedTextures = new ArrayList<>();
    protected TextureRegion idleFrame;
    private static final float START_X = 540f; // À droite (800 - 3*60 - 2*10 - 20)
    private static final float UNIT_BUTTONS_Y = 520f; // En haut

    // Constructeur pour bouton de type d'unité
    public UnitShopButton(float x, float y, float width, float height, Base.Type unitType) {
        this.bounds = new Rectangle(x, y, width, height);
        this.unitType = unitType;
        this.spawnIndex = null;
        this.buttonType = ButtonType.UNIT_TYPE;
        this.selected = false;
        this.borderColor = Color.CLEAR;
        this.font = new BitmapFont();
        this.font.getData().setScale(1.2f);

        switch (unitType) {
            case MELEE:
                this.label = "Melee";
                break;
            case SNIPER:
                this.label = "Sniper";
                break;
            case TANK:
                this.label = "Tank";
                break;
            default:
                this.label = "?";
                break;
        }

        Texture idleTex = new Texture(Gdx.files.internal("Frames/" + this.label +".png"));
        this.idleFrame = new TextureRegion(idleTex);
    }

    // Constructeur pour bouton de point de spawn
    public UnitShopButton(float x, float y, float width, float height, int spawnIndex) {
        this.bounds = new Rectangle(x, y, width, height);
        this.unitType = null;
        this.spawnIndex = spawnIndex;
        this.buttonType = ButtonType.SPAWN_POINT;
        this.selected = false;
        this.color = Color.CLEAR;
        this.borderColor = Color.CLEAR;
        this.label = "S" + (spawnIndex + 1);
        this.font = new BitmapFont();
        this.font.getData().setScale(1.2f);

        Texture idleTex = new Texture(Gdx.files.internal("Frames/Melee.png"));
        this.idleFrame = new TextureRegion(idleTex);
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {

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
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        if (selected) {
            shapeRenderer.setColor(Color.WHITE);
            // Dessiner une bordure plus épaisse
            for (int i = 0; i < 3; i++) {
                shapeRenderer.rect(bounds.x - i, bounds.y - i, bounds.width + i * 2, bounds.height + i * 2);
            }
        } else {
            shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        shapeRenderer.end();

        // Dessiner le label
        batch.begin();
        font.setColor(Color.WHITE);

        if (buttonType == ButtonType.UNIT_TYPE && currentFrame != null) {
            batch.draw(currentFrame, drawX, drawY, drawW, drawH);
        } else {
            // Pour les spawn buttons → afficher du texte
            font.draw(batch, label, bounds.x + 10, bounds.y + 25);
        }
        batch.end();
    }

    public boolean isClicked(float touchX, float touchY) {
        return bounds.contains(touchX, touchY);
    }

    public Base.Type getUnitType() {
        return unitType;
    }

    public Integer getSpawnIndex() {
        return spawnIndex;
    }

    public ButtonType getButtonType() {
        return buttonType;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}