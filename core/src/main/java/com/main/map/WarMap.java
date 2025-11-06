package com.main.map;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;


public class WarMap {
    private int mapHeight;
    private int mapWidth;
    private int tileWidth;
    private int tileHeight;
    private List<Obstacle> obstacles;
    private TiledMap tiledMap;
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
    }

    public void render(){
        if (renderer != null){
            renderer.render();
        }
    }

     public void setView(com.badlogic.gdx.graphics.OrthographicCamera camera) {
        if (renderer != null) {
            renderer.setView(camera);
        }
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