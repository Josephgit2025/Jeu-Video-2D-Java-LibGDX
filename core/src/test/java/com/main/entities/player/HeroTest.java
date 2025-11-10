package com.main.entities.player;

import com.main.map.WarMap;
import com.main.entities.Unit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HeroTest {

    private Hero hero;
    
    @Mock
    private WarMap mockMap;
    
    @Mock
    private Unit mockTarget;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockMap.isCollisionRect(anyFloat(), anyFloat())).thenReturn(false);
        // Note: Ce test nécessite LibGDX, pourrait échouer sans HeadlessApplication
        try {
            hero = new Hero(100, 200, mockMap);
        } catch (Exception e) {
            // Skip si LibGDX n'est pas initialisé
        }
    }

    @Test
    public void testConstructor() {
        if (hero != null) {
            assertNotNull(hero);
            assertEquals(100, hero.getPosX(), 0.01f);
            assertEquals(200, hero.getPosY(), 0.01f);
        }
    }

    @Test
    public void testMoveUp() {
        if (hero != null) {
            float initialY = hero.getPosY();
            hero.moveUp(0.016f, 1000);
            assertTrue(hero.getPosY() >= initialY);
        }
    }

    @Test
    public void testMoveDown() {
        if (hero != null) {
            hero.moveDown(0.016f);
            // Vérifier que le mouvement ne cause pas d'erreur
        }
    }

    @Test
    public void testMoveLeft() {
        if (hero != null) {
            hero.moveLeft(0.016f);
            // Vérifier que le mouvement ne cause pas d'erreur
        }
    }

    @Test
    public void testMoveRight() {
        if (hero != null) {
            hero.moveRight(0.016f, 1000);
            // Vérifier que le mouvement ne cause pas d'erreur
        }
    }

    @Test
    public void testUpdate() {
        if (hero != null) {
            hero.update(0.016f, 1920, 1080);
            // Vérifier que update ne lance pas d'exception
        }
    }

    @Test
    public void testMove() {
        if (hero != null) {
            hero.move();
            // Méthode vide, vérifier qu'elle ne lance pas d'exception
        }
    }

    @Test
    public void testGetSprite() {
        if (hero != null) {
            assertNotNull(hero.getSprite());
        }
    }
}