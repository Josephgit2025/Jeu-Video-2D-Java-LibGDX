package com.main.entities.player;

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.main.entities.Unit;
import com.main.map.WarMap;
import com.main.weapons.Pistol;

public class HeroTest {

    private static Application application;
    private Hero hero;
    
    @Mock
    private WarMap mockMap;
    
    @Mock
    private Unit mockTarget;

    @Mock
    private GL20 mockGL;

    @BeforeClass
    public static void init() {
        application = new HeadlessApplication(new com.badlogic.gdx.ApplicationAdapter() {});
    }

    @AfterClass
    public static void cleanUp() {
        if (application != null) {
            application.exit();
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Gdx.gl = mockGL;
        Gdx.gl20 = mockGL;
        
        when(mockMap.isCollisionRect(anyFloat(), anyFloat(), anyFloat(), anyFloat())).thenReturn(false);
        
        hero = new Hero(100, 200, mockMap);
        hero.setWeapon(new Pistol());
    }

    @Test
    public void testConstructorInitializesHeroCorrectly() {
        assertNotNull("Hero should not be null", hero);
        assertEquals("Initial X position should be 100", 100, hero.getPosX(), 0.01f);
        assertEquals("Initial Y position should be 200", 200, hero.getPosY(), 0.01f);
        assertEquals("Hero health should be 500", 500, hero.getHealth());
        assertNotNull("Hero should have a weapon", hero.weapon);
        assertEquals("Hero speed should be 8", 8, hero.getSpeed(), 0.01f);
    }

    @Test
    public void testMoveUpIncreasesYPosition() {
        float initialY = hero.getPosY();
        hero.moveUp(0.016f, 1000);
        assertTrue("Y position should increase when moving up", hero.getPosY() > initialY);
    }

    @Test
    public void testMoveUpWithCollisionDoesNotMove() {
        when(mockMap.isCollisionRect(anyFloat(), anyFloat(), anyFloat(), anyFloat())).thenReturn(true);
        float initialY = hero.getPosY();
        hero.moveUp(0.016f, 1000);
        assertEquals("Y position should not change with collision", initialY, hero.getPosY(), 0.01f);
    }

    @Test
    public void testMoveUpRespectsBoundary() {
        hero.setSpritePosY(995);
        hero.moveUp(0.016f, 1000);
        assertTrue("Hero should not exceed map height", hero.getPosY() + hero.getHeight() <= 1000);
    }

    @Test
    public void testMoveDownDecreasesYPosition() {
        float initialY = hero.getPosY();
        hero.moveDown(0.016f);
        assertTrue("Y position should decrease when moving down", hero.getPosY() < initialY);
    }

    @Test
    public void testMoveDownWithCollisionDoesNotMove() {
        when(mockMap.isCollisionRect(anyFloat(), anyFloat(), anyFloat(), anyFloat())).thenReturn(true);
        float initialY = hero.getPosY();
        hero.moveDown(0.016f);
        assertEquals("Y position should not change with collision", initialY, hero.getPosY(), 0.01f);
    }

    @Test
    public void testMoveDownRespectsBoundary() {
        hero.setSpritePosY(5);
        hero.moveDown(0.016f);
        assertTrue("Hero Y should not go below 0", hero.getPosY() >= 0);
    }

    @Test
    public void testMoveLeftDecreasesXPosition() {
        float initialX = hero.getPosX();
        hero.moveLeft(0.016f);
        assertTrue("X position should decrease when moving left", hero.getPosX() < initialX);
    }

    @Test
    public void testMoveLeftWithCollisionDoesNotMove() {
        when(mockMap.isCollisionRect(anyFloat(), anyFloat(), anyFloat(), anyFloat())).thenReturn(true);
        float initialX = hero.getPosX();
        hero.moveLeft(0.016f);
        assertEquals("X position should not change with collision", initialX, hero.getPosX(), 0.01f);
    }

    @Test
    public void testMoveLeftRespectsBoundary() {
        hero.setSpritePosX(5);
        hero.moveLeft(0.016f);
        assertTrue("Hero X should not go below 0", hero.getPosX() >= 0);
    }

    @Test
    public void testMoveRightIncreasesXPosition() {
        float initialX = hero.getPosX();
        hero.moveRight(0.016f, 1000);
        assertTrue("X position should increase when moving right", hero.getPosX() > initialX);
    }

    @Test
    public void testMoveRightWithCollisionDoesNotMove() {
        when(mockMap.isCollisionRect(anyFloat(), anyFloat(), anyFloat(), anyFloat())).thenReturn(true);
        float initialX = hero.getPosX();
        hero.moveRight(0.016f, 1000);
        assertEquals("X position should not change with collision", initialX, hero.getPosX(), 0.01f);
    }

    @Test
    public void testMoveRightRespectsBoundary() {
        hero.setSpritePosX(965);
        hero.moveRight(0.016f, 1000);
        assertTrue("Hero should not exceed map width", hero.getPosX() + hero.getWidth() <= 1000);
    }

    @Test
    public void testUpdateDoesNotThrowException() {
        hero.update(0.016f, 1920, 1080);
        assertNotNull("Hero should still exist after update", hero);
    }

    @Test
    public void testMoveMethodDoesNothing() {
        float initialX = hero.getPosX();
        float initialY = hero.getPosY();
        hero.move(0.016f);
        assertEquals("X should not change with move()", initialX, hero.getPosX(), 0.01f);
        assertEquals("Y should not change with move()", initialY, hero.getPosY(), 0.01f);
    }

    @Test
    public void testGetSpriteReturnsValidSprite() {
        assertNotNull("Sprite should not be null", hero.getSprite());
    }

    @Test
    public void testAttackWithNullTargetDoesNothing() {
        hero.attack();
        // Pas d'exception = succès
    }

    @Test
    public void testAttackWithDeadTargetDoesNothing() {
        when(mockTarget.isDead()).thenReturn(true);
        hero.setTarget(mockTarget);
        hero.attack();
        verify(mockTarget, never()).takeDamage(anyInt());
    }

    @Test
    public void testAttackWithLiveTargetDealsDamage() {
        when(mockTarget.isDead()).thenReturn(false);
        hero.setTarget(mockTarget);
        hero.setCooldown(0);
        
        // S'assurer que l'arme a des munitions
        int initialMunitions = hero.weapon.getMunitions();
        assertTrue("Weapon should have munitions", initialMunitions > 0);
        
        hero.attack();
        
        verify(mockTarget, times(1)).takeDamage(anyInt());
    }

    @Test
    public void testAttackRespectsAttackCooldown() {
        when(mockTarget.isDead()).thenReturn(false);
        hero.setTarget(mockTarget);
        hero.setCooldown(5);
        
        hero.attack();
        
        verify(mockTarget, never()).takeDamage(anyInt());
    }

    @Test
    public void testWeaponReloadWhenNoMunitions() {
        when(mockTarget.isDead()).thenReturn(false);
        hero.setTarget(mockTarget);
        hero.setCooldown(0);
        
        // Vider les munitions
        while (hero.weapon.getMunitions() > 0) {
            hero.weapon.attack();
        }
        
        assertEquals("Weapon should have 0 munitions", 0, hero.weapon.getMunitions());
        
        hero.attack();
        
        // Vérifier qu'on ne peut pas attaquer sans munitions
        verify(mockTarget, never()).takeDamage(anyInt());
    }

    @Test
    public void testHeroHasCorrectInitialHealth() {
        assertEquals("Hero should start with 500 health", 500, hero.getHealth());
    }

    @Test
    public void testHeroHasCorrectSpeed() {
        assertEquals("Hero speed should be 8", 8, hero.getSpeed(), 0.01f);
    }

    @Test
    public void testHeroHasCorrectAttackSpeed() {
        assertEquals("Hero attack speed should be 1", 1.0f, hero.getAttackSpeed(), 0.01f);
    }

    @Test
    public void testHeroWeaponIsNotNull() {
        assertNotNull("Hero weapon should not be null", hero.weapon);
    }

    @Test
    public void testAttackReducesWeaponMunitions() {
        when(mockTarget.isDead()).thenReturn(false);
        hero.setTarget(mockTarget);
        hero.setCooldown(0);
        
        int initialMunitions = hero.weapon.getMunitions();
        hero.attack();
        
        // Après une attaque, les munitions diminuent ET le cooldown est remis
        assertTrue("Munitions should decrease after attack", hero.weapon.getMunitions() < initialMunitions);
        assertTrue("Cooldown should be set after attack", hero.getAttackCooldown() > 0);
    }

    @Test
    public void testMultipleAttacksDepleteMunitions() {
        when(mockTarget.isDead()).thenReturn(false);
        hero.setTarget(mockTarget);
        
        int attackCount = 0;
        int initialMunitions = hero.weapon.getMunitions();
        
        // Simuler plusieurs attaques en réinitialisant le cooldown à chaque fois
        while (hero.weapon.getMunitions() > 0 && attackCount < initialMunitions) {
            hero.setCooldown(0); // Réinitialiser le cooldown avant chaque attaque
            hero.attack();
            attackCount++;
        }
        
        assertTrue("Should have attacked multiple times", attackCount > 0);
        assertEquals("Should have no munitions left", 0, hero.weapon.getMunitions());
    }

    @Test
    public void testAttackSetsCooldownAfterAttack() {
        when(mockTarget.isDead()).thenReturn(false);
        hero.setTarget(mockTarget);
        hero.setCooldown(0);
        
        hero.attack();
        
        // Après une attaque, le cooldown devrait être égal à l'attackSpeed de l'arme
        assertEquals("Cooldown should be set to weapon attack speed", 
                     hero.weapon.getAttackSpeed(), 
                     hero.getAttackCooldown());
    }

    @Test
    public void testCannotAttackTwiceWithoutResettingCooldown() {
        when(mockTarget.isDead()).thenReturn(false);
        hero.setTarget(mockTarget);
        hero.setCooldown(0);
        
        int initialMunitions = hero.weapon.getMunitions();
        
        // Première attaque
        hero.attack();
        assertEquals("Munitions should decrease by 1", initialMunitions - 1, hero.weapon.getMunitions());
        
        // Deuxième attaque SANS réinitialiser le cooldown
        hero.attack();
        assertEquals("Munitions should NOT decrease again due to cooldown", 
                     initialMunitions - 1, 
                     hero.weapon.getMunitions());
        
        // Vérifier qu'on a bien attaqué qu'une seule fois
        verify(mockTarget, times(1)).takeDamage(anyInt());
    }

    @Test
    public void testGettersReturnCorrectValues() {
        assertEquals("getPosX should return correct value", 100, hero.getPosX(), 0.01f);
        assertEquals("getPosY should return correct value", 200, hero.getPosY(), 0.01f);
        assertEquals("getSpeed should return correct value", 8, hero.getSpeed(), 0.01f);
        assertEquals("getHealth should return correct value", 500, hero.getHealth());
        assertEquals("getAttackSpeed should return correct value", 1, hero.getAttackSpeed(), 0.01f);
    }
}