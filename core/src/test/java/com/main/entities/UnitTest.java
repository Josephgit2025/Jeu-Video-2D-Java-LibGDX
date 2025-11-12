package com.main.entities;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UnitTest {
    @Test
    public void testAttackTargetOutOfRange() {
        // Place la cible hors de portée
        unit.target = enemy2; // enemy2 est loin
        unit.attackCooldown = 0;
        int initialHealth = enemy2.health;
        unit.attack();
        // L'attaque ne doit pas infliger de dégâts ni changer le cooldown
        assertEquals(initialHealth, enemy2.health);
        assertEquals(0, unit.attackCooldown);
    }

    private static HeadlessApplication application;
    private TestUnit unit;
    private TestUnit enemy1;
    private TestUnit enemy2;
    
    @Mock
    private SpriteBatch mockBatch;

    @BeforeClass
    public static void init() {
        // Initialiser LibGDX en mode headless pour les tests
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        application = new HeadlessApplication(new ApplicationAdapter() {}, config);
        
        // Mock GL20 pour éviter les erreurs de rendu
        Gdx.gl20 = mock(GL20.class);
        Gdx.gl = Gdx.gl20;
    }

    // Classe concrète pour tester la classe abstraite Unit
    private class TestUnit extends Unit {
        public TestUnit(float posX, float posY) {
            super(null, posX, posY);
            this.texture = mock(Texture.class);
            this.sprite = mock(Sprite.class);
            when(this.sprite.getX()).thenReturn(posX);
            when(this.sprite.getY()).thenReturn(posY);
            this.health = 100;
            this.attackDamage = 10;
            this.attackSpeed = 5;
            this.speed = 2.0f;
            this.range = 50;
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        unit = new TestUnit(100, 200);
        enemy1 = new TestUnit(120, 220);
        enemy2 = new TestUnit(500, 500);
    }

    @Test
    public void testConstructor() {
        assertNotNull(unit);
        assertEquals(100, unit.getPosX(), 0.01f);
        assertEquals(200, unit.getPosY(), 0.01f);
        assertNotNull(unit.getSprite());
    }

    @Test
    public void testGetPosX() {
        assertEquals(100, unit.getPosX(), 0.01f);
    }

    @Test
    public void testGetPosY() {
        assertEquals(200, unit.getPosY(), 0.01f);
    }

    @Test
    public void testGetSprite() {
        assertNotNull(unit.getSprite());
    }

    @Test
    public void testSetSpritePosX() {
        unit.setSpritePosX(150);
        assertEquals(150, unit.getPosX(), 0.01f);
    }

    @Test
    public void testSetSpritePosY() {
        unit.setSpritePosY(250);
        assertEquals(250, unit.getPosY(), 0.01f);
    }

    @Test
    public void testTakeDamage() {
        unit.takeDamage(30);
        assertEquals(70, unit.health);
    }

    @Test
    public void testTakeDamageToZero() {
        unit.takeDamage(100);
        assertEquals(0, unit.health);
        assertTrue(unit.isDead());
    }

    @Test
    public void testTakeDamageOverKill() {
        unit.takeDamage(150);
        assertEquals(0, unit.health);
        assertTrue(unit.isDead());
    }

    @Test
    public void testIsDead() {
        assertFalse(unit.isDead());
        unit.takeDamage(100);
        assertTrue(unit.isDead());
    }

    @Test
    public void testIsNotDead() {
        unit.takeDamage(50);
        assertFalse(unit.isDead());
    }

    @Test
    public void testUpdateCooldown() {
        unit.attackCooldown = 10;
        unit.updateCooldown();
        assertEquals(9, unit.attackCooldown);
        
        unit.updateCooldown();
        assertEquals(8, unit.attackCooldown);
    }

    @Test
    public void testUpdateCooldownAtZero() {
        unit.attackCooldown = 0;
        unit.updateCooldown();
        assertEquals(0, unit.attackCooldown);
    }

    @Test
    public void testUpdateCooldownMultipleTimes() {
        unit.attackCooldown = 10;
        for (int i = 0; i < 5; i++) {
            unit.updateCooldown();
        }
        assertEquals(5, unit.attackCooldown);
    }

    @Test
    public void testDetectEnemiesInRange() {
        List<Unit> enemies = new ArrayList<>();
        enemies.add(enemy1);
        enemies.add(enemy2);
        
        List<Unit> inRange = unit.detectEnemiesInRange(enemies);
        
        assertEquals(1, inRange.size());
        assertTrue(inRange.contains(enemy1));
        assertFalse(inRange.contains(enemy2));
    }

    @Test
    public void testDetectEnemiesInRangeEmpty() {
        List<Unit> enemies = new ArrayList<>();
        List<Unit> inRange = unit.detectEnemiesInRange(enemies);
        
        assertEquals(0, inRange.size());
    }

    @Test
    public void testDetectEnemiesInRangeNoEnemies() {
        List<Unit> enemies = new ArrayList<>();
        enemies.add(enemy2);
        
        List<Unit> inRange = unit.detectEnemiesInRange(enemies);
        
        assertEquals(0, inRange.size());
    }

    @Test
    public void testDetectEnemiesInRangeWithSelf() {
        List<Unit> enemies = new ArrayList<>();
        enemies.add(unit);
        enemies.add(enemy1);
        
        List<Unit> inRange = unit.detectEnemiesInRange(enemies);
        
        // Ne doit pas se détecter lui-même
        assertEquals(1, inRange.size());
        assertFalse(inRange.contains(unit));
        assertTrue(inRange.contains(enemy1));
    }

    @Test
    public void testSelectTarget() {
        List<Unit> enemies = new ArrayList<>();
        enemies.add(enemy1);
        enemies.add(enemy2);
        
        unit.selectTarget(enemies);
        
        assertNotNull(unit.target);
        assertEquals(enemy1, unit.target);
    }

    @Test
    public void testSelectTargetNoEnemies() {
        List<Unit> enemies = new ArrayList<>();
        
        unit.selectTarget(enemies);
        
        assertNull(unit.target);
    }

    @Test
    public void testSelectTargetOutOfRange() {
        List<Unit> enemies = new ArrayList<>();
        enemies.add(enemy2);
        
        unit.selectTarget(enemies);
        
        assertNull(unit.target);
    }

    @Test
    public void testSelectTargetClosest() {
        TestUnit nearEnemy = new TestUnit(110, 205);
        List<Unit> enemies = new ArrayList<>();
        enemies.add(enemy1);
        enemies.add(nearEnemy);
        
        unit.selectTarget(enemies);
        
        // Devrait sélectionner le plus proche
        assertEquals(nearEnemy, unit.target);
    }

    @Test
    public void testAttackWithTarget() {
        unit.target = enemy1;
        unit.attackCooldown = 0;
        int initialHealth = enemy1.health;
        
        unit.attack();
        
        assertEquals(initialHealth - unit.attackDamage, enemy1.health);
        assertEquals(unit.attackSpeed, unit.attackCooldown);
    }

    @Test
    public void testAttackWithoutTarget() {
        unit.target = null;
        unit.attackCooldown = 0;
        
        unit.attack();
        
        assertEquals(0, unit.attackCooldown);
    }

    @Test
    public void testAttackWithDeadTarget() {
        unit.target = enemy1;
        enemy1.health = 0;
        unit.attackCooldown = 0;
        
        unit.attack();
        
        assertEquals(0, unit.attackCooldown);
    }

    @Test
    public void testAttackOnCooldown() {
        unit.target = enemy1;
        unit.attackCooldown = 5;
        int initialHealth = enemy1.health;
        
        unit.attack();
        
        assertEquals(initialHealth, enemy1.health);
        assertEquals(5, unit.attackCooldown);
    }

    @Test
    public void testAttackMultipleTimes() {
        unit.target = enemy1;
        unit.attackCooldown = 0;
        
        unit.attack();
        unit.updateCooldown();
        unit.updateCooldown();
        unit.updateCooldown();
        unit.updateCooldown();
        unit.updateCooldown();
        
        // Cooldown devrait être à 0
        assertEquals(0, unit.attackCooldown);
        
        // Peut attaquer à nouveau
        int healthBefore = enemy1.health;
        unit.attack();
        assertEquals(healthBefore - unit.attackDamage, enemy1.health);
    }

    @Test
    public void testSpecialAbility() {
        unit.specialAbility();
        // Vérifie que ça ne lance pas d'exception
    }

    @Test
    public void testOnDeath() {
        unit.takeDamage(100);
        assertTrue(unit.isDead());
        assertEquals(0, unit.health);
    }

    @Test
    public void testMove() {
        float initialX = unit.getPosX();
        unit.move(1.0f);
        assertEquals(initialX + unit.speed, unit.getPosX(), 0.01f);
    }

    @Test
    public void testMoveWithDelta() {
        float initialX = unit.getPosX();
        unit.move(0.5f);
        assertEquals(initialX + (unit.speed * 0.5f), unit.getPosX(), 0.01f);
    }

    @Test
    public void testMoveWithZeroDelta() {
        float initialX = unit.getPosX();
        unit.move(0.0f);
        assertEquals(initialX, unit.getPosX(), 0.01f);
    }

    @Test
    public void testMoveMultipleTimes() {
        float initialX = unit.getPosX();
        unit.move(1.0f);
        unit.move(1.0f);
        unit.move(1.0f);
        assertEquals(initialX + (unit.speed * 3), unit.getPosX(), 0.01f);
    }

    @Test
    public void testWidthAndHeight() {
        assertEquals(32, unit.width, 0.01f);
        assertEquals(48, unit.height, 0.01f);
    }
}