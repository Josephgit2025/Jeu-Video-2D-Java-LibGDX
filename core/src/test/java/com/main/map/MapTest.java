package com.main.map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.main.utils.Position;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MapTest {
    
    private static HeadlessApplication application;
    
    @BeforeClass
    public static void init() {
        application = new HeadlessApplication(new ApplicationAdapter() {});
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);
    }
    
    @AfterClass
    public static void cleanup() {
        if (application != null) {
            application.exit();
        }
    }
    
    @Test
    public void testBaseCreation() {
        Base base = new Base(100, 200);
        
        assertNotNull("Base should not be null", base);
        assertEquals(100.0f, base.getPosX(), 0.01f);
        assertEquals(200.0f, base.getPosY(), 0.01f);
    }
    
    @Test
    public void testObstacleCreation() {
        Position pos = new Position(50, 75);
        Obstacle obstacle = new Obstacle(pos, 100, 150);
        
        assertNotNull("Obstacle should not be null", obstacle);
        assertEquals(100.0, obstacle.getWidth(), 0.01);
        assertEquals(150.0, obstacle.getHeight(), 0.01);
    }
    
    @Test
    public void testWarMapCreation() {
        WarMap map = new WarMap();
        
        assertNotNull("WarMap should not be null", map);
        assertTrue("Map width should be positive", map.getMapWidthInPixels() > 0);
        assertTrue("Map height should be positive", map.getMapHeightInPixels() > 0);
    }
    
    @Test
    public void testWarMapHasObstacles() {
        WarMap map = new WarMap();
        
        assertNotNull("Obstacles list should not be null", map.getObstacles());
    }
}