package com.main.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class WarMap {
    private int mapHeight;
    private int mapWidth;
    private int tileWidth;
    private int tileHeight;
    private List<Obstacle> obstacles;
    private TiledMap tiledMap;
    private TiledMapTileLayer collisionLayer;
    private OrthogonalTiledMapRenderer renderer;
    private TmxMapLoader mapLoader;

    public WarMap(){
        this.obstacles = new ArrayList<>();
        loadTmxMap();
        render();
    }

    private void loadTmxMap(){
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("map/map.tmx");

        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        this.mapHeight = tiledMap.getProperties().get("height", Integer.class);
        this.mapWidth = tiledMap.getProperties().get("width", Integer.class);
        this.tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        this.tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("collision");
    }

    public void render(){
        if (renderer != null){
            renderer.render();
        }
    }

    public boolean isCollision(float posX, float posY){
        if (collisionLayer == null)
            return false;
        int tileX = (int) (posX / tileWidth);
        int tileY = (int) (posY / tileWidth);
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
        return cell != null && cell.getTile() != null;
    }

    public boolean isCollisionRect(float posX, float posY, float width, float height){
        return isCollision(posX, posY) || isCollision(posX + width, posY) || isCollision(posX, posY + height) || isCollision(posX + width, posY + height);
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
        return mapWidth * tileWidth;
    }

    public int getMapHeightInPixels() {
        return mapHeight * tileHeight;
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

    public void dispose(){
        if (tiledMap != null){
            tiledMap.dispose();
        }
        if (renderer != null){
            renderer.dispose();
        }
    }
}