package com.main.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;


public class WarMap {
    private int mapHeight;
    private int mapWidth;
    private int tileWidth;
    private int tileHeight;
    private float scale = 2.0f; // Scale factor pour agrandir la map
    private List<Rectangle> collisionRects;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private TmxMapLoader mapLoader;

    public WarMap(){
        this.collisionRects = new ArrayList<>();
        loadTmxMap();
        render();
    }

    private void loadTmxMap(){
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("map/JAVAGAMEZ.tmx");

        // Scale factor pour agrandir la map (2 = 2x plus grand)
        float scale = 2.0f;
        renderer = new OrthogonalTiledMapRenderer(tiledMap, scale);
        this.mapHeight = tiledMap.getProperties().get("height", Integer.class);
        this.mapWidth = tiledMap.getProperties().get("width", Integer.class);
        this.tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        this.tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        
        // Charger les objets de collision depuis le layer "COLLISION"
        loadCollisionObjects();
    }

    private void loadCollisionObjects() {
        MapObjects objects = tiledMap.getLayers().get("collision").getObjects();
        
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                // Scaler et inverser Y pour correspondre au système LibGDX
                // Tiled: Y croît vers le bas, LibGDX: Y croît vers le haut
                float mapHeightPixels = mapHeight * tileHeight;
                Rectangle scaledRect = new Rectangle(
                    rect.x * scale,
                    (mapHeightPixels - rect.y - rect.height) * scale, // Inverser Y
                    rect.width * scale,
                    rect.height * scale
                );
                collisionRects.add(scaledRect);
            }
        }
    }

    public void render(){
        if (renderer != null){
            renderer.render();
        }
    }

    public boolean isCollision(float posX, float posY){
        // Vérifier si le point (posX, posY) est dans un rectangle de collision
        for (Rectangle rect : collisionRects) {
            if (rect.contains(posX, posY)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCollisionRect(float x, float y, float width, float height){
        // Vérifier si un rectangle (joueur, ennemi) chevauche un obstacle
        Rectangle entityRect = new Rectangle(x, y, width, height);
        for (Rectangle rect : collisionRects) {
            if (entityRect.overlaps(rect)) {
                return true;
            }
        }
        return false;
    }

    public void setView(com.badlogic.gdx.graphics.OrthographicCamera camera) {
        if (renderer != null) {
            renderer.setView(camera);
        }
    }

    public TiledMap getMap(){
        return this.tiledMap;
    }

    public int getMapWidthInPixels() {
        return (int)(mapWidth * tileWidth * scale);
    }

    public int getMapHeightInPixels() {
        return (int)(mapHeight * tileHeight * scale);
    }

    public int getMapHeight() {
        return this.mapHeight;
    }

    public int getMapWidth() {
        return this.mapWidth;
    }

    public void dispose(){
        if (tiledMap != null){
            tiledMap.dispose();
        }
        if (renderer != null){
            renderer.dispose();
        }
    }
}