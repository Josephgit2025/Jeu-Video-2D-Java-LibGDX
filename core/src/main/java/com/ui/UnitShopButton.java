package com.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.main.map.Base;

public class UnitShopButton {

    public enum ButtonType {
        UNIT_TYPE,    // Bouton pour sélectionner le type d'unité
        SPAWN_POINT   // Bouton pour sélectionner le point de spawn
    }

    private Rectangle bounds;
    private Base.Type unitType;
    private Integer spawnIndex;  // Nullable pour les boutons de type d'unité
    private Color color;
    private Color borderColor;
    private ButtonType buttonType;
    private boolean selected;
    private String label;
    private BitmapFont font;

    // Constructeur pour bouton de type d'unité
    public UnitShopButton(float x, float y, float width, float height, Base.Type unitType) {
        this.bounds = new Rectangle(x, y, width, height);
        this.unitType = unitType;
        this.spawnIndex = null;
        this.buttonType = ButtonType.UNIT_TYPE;
        this.selected = false;
        this.borderColor = Color.BLACK;
        this.font = new BitmapFont();
        this.font.getData().setScale(1.2f);

        switch (unitType) {
            case MELEE:
                this.color = Color.GREEN;
                this.label = "Melee";
                break;
            case SNIPER:
                this.color = Color.BLUE;
                this.label = "Sniper";
                break;
            case TANK:
                this.color = Color.RED;
                this.label = "Tank";
                break;
            default:
                this.color = Color.GRAY;
                this.label = "?";
                break;
        }
    }

    // Constructeur pour bouton de point de spawn
    public UnitShopButton(float x, float y, float width, float height, int spawnIndex) {
        this.bounds = new Rectangle(x, y, width, height);
        this.unitType = null;
        this.spawnIndex = spawnIndex;
        this.buttonType = ButtonType.SPAWN_POINT;
        this.selected = false;
        this.color = Color.ORANGE;
        this.borderColor = Color.BLACK;
        this.label = "S" + (spawnIndex + 1);
        this.font = new BitmapFont();
        this.font.getData().setScale(1.2f);
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        // Dessiner le rectangle du bouton
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();

        // Bordure (plus épaisse si sélectionné)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        if (selected) {
            shapeRenderer.setColor(Color.YELLOW);
            // Dessiner une bordure plus épaisse
            for (int i = 0; i < 3; i++) {
                shapeRenderer.rect(bounds.x - i, bounds.y - i, bounds.width + i * 2, bounds.height + i * 2);
            }
        } else {
            shapeRenderer.setColor(borderColor);
            shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        shapeRenderer.end();
        
        // Dessiner le label
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, label, 
            bounds.x + bounds.width / 2 - 10, 
            bounds.y + bounds.height / 2 + 5);
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