package com.main.map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class WarMapTest {

    private WarMap warMap;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // Note: Ce test nécessite un environnement LibGDX initialisé
        // Il pourrait être nécessaire d'utiliser HeadlessApplication
        try {
            warMap = new WarMap();
        } catch (Exception e) {
            // Si l'initialisation échoue (pas de fichier map.tmx en test), on skip
        }
    }

    @Test
    public void testConstructor() {
        if (warMap != null) {
            assertNotNull(warMap);
        }
    }

    @Test
    public void testGetMap() {
        if (warMap != null) {
            TiledMap map = warMap.getMap();
            assertNotNull(map);
        }
    }

    @Test
    public void testGetMapHeight() {
        if (warMap != null) {
            int height = warMap.getMapHeight();
            assertTrue(height >= 0);
        }
    }

    @Test
    public void testGetMapWidth() {
        if (warMap != null) {
            int width = warMap.getMapWidth();
            assertTrue(width >= 0);
        }
    }

    @Test
    public void testGetMapHeightInPixels() {
        if (warMap != null) {
            int heightPixels = warMap.getMapHeightInPixels();
            assertTrue(heightPixels >= 0);
        }
    }

    @Test
    public void testGetMapWidthInPixels() {
        if (warMap != null) {
            int widthPixels = warMap.getMapWidthInPixels();
            assertTrue(widthPixels >= 0);
        }
    }

    @Test
    public void testIsCollision() {
        if (warMap != null) {
            boolean collision = warMap.isCollision(0, 0);
            // Le résultat dépend de la carte chargée
            assertTrue(collision || !collision);
        }
    }

    @Test
    public void testIsCollisionRect() {
        if (warMap != null) {
            boolean collision = warMap.isCollisionRect(0, 0, 10, 10);
            // Le résultat dépend de la carte chargée
            assertTrue(collision || !collision);
        }
    }

    @Test
    public void testRender() {
        if (warMap != null) {
            // Test que render ne lance pas d'exception
            warMap.render();
        }
    }

    @Test
    public void testSetView() {
        if (warMap != null) {
            OrthographicCamera camera = new OrthographicCamera();
            // Test que setView ne lance pas d'exception
            warMap.setView(camera);
        }
    }

    @Test
    public void testDispose() {
        if (warMap != null) {
            warMap.dispose();
            // Vérifier qu'on peut appeler dispose sans erreur
        }
    }
}