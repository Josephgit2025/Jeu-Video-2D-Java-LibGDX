package com.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private Vector2 position;
    private Vector2 velocity;
    private Texture texture;
    private float speed;
    private float width, height;

    public Hero(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2();
        speed = 200f; // pixels par seconde
        
        // Charger la texture du héros
        texture = new Texture("libgdx.png"); // Tu peux remplacer par ton sprite
        width = texture.getWidth();
        height = texture.getHeight();
    }

    public void update(float delta) {
        handleInput();
        
        // Appliquer le mouvement
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        
        // Limites de l'écran
        clampToScreen();
    }

    private void handleInput() {
        velocity.set(0, 0); // Reset velocity
        
        // Déplacement avec les flèches ou WASD
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x = -speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x = speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y = speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y = -speed;
        }
    }

    private void clampToScreen() {
        // Empêcher le héros de sortir de l'écran
        if (position.x < 0) position.x = 0;
        if (position.y < 0) position.y = 0;
        if (position.x + width > Gdx.graphics.getWidth()) {
            position.x = Gdx.graphics.getWidth() - width;
        }
        if (position.y + height > Gdx.graphics.getHeight()) {
            position.y = Gdx.graphics.getHeight() - height;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }

    // Getters pour la position (utile pour caméra, collisions, etc.)
    public Vector2 getPosition() {
        return position;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}