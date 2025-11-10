package com.main.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.MapObject;
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
    private List<Obstacle> obstacles;
    private List<Rectangle> collisionRects;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private TmxMapLoader mapLoader;

    public WarMap(){
        this.obstacles = new ArrayList<>();
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
        // Récupérer le layer d'objets de collision
        String[] possibleNames = {"collision", "colision", "Calque d'Objets 1", "COLLISION"};
        
        for (String layerName : possibleNames) {
            if (tiledMap.getLayers().get(layerName) != null) {
                System.out.println("Found collision layer: " + layerName);
                
                // Parcourir tous les objets du layer
                for (MapObject object : tiledMap.getLayers().get(layerName).getObjects()) {
                    
                    // Récupérer les propriétés de l'objet
                    float x = object.getProperties().get("x", Float.class);
                    float y = object.getProperties().get("y", Float.class);
                    float width = object.getProperties().get("width", Float.class);
                    float height = object.getProperties().get("height", Float.class);
                    
                    // Ne pas modifier Y - les coordonnées de Tiled semblent déjà correctes pour LibGDX
                    
                    // DEBUG: Afficher les premières collisions
                    if (collisionRects.size() < 3) {
                        System.out.println("Collision object: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
                        System.out.println("Scaled: x=" + (x*scale) + ", y=" + (y*scale) + ", w=" + (width*scale) + ", h=" + (height*scale));
                    }
                    
                    // Appliquer le scale aux rectangles
                    Rectangle scaledRect = new Rectangle(
                        x * scale,
                        y * scale,
                        width * scale,
                        height * scale
                    );
                    
                    collisionRects.add(scaledRect);
                }
                
                System.out.println("Loaded " + collisionRects.size() + " collision objects");
                System.out.println("Map dimensions: " + mapWidth + "x" + mapHeight + " tiles");
                System.out.println("Tile size: " + tileWidth + "x" + tileHeight + " pixels");
                System.out.println("Scale factor: " + scale);
                System.out.println("Map size in pixels (scaled): " + (mapWidth*tileWidth*scale) + "x" + (mapHeight*tileHeight*scale));
                return;
            }
        }
        
        System.out.println("Warning: No collision layer found!");
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

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Rectangle> getCollisionRects() {
        return collisionRects;
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