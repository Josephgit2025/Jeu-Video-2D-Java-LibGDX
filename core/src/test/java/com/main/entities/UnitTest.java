package com.main.entities;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
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
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UnitTest {

    private static Application application;
    private TestUnit unit;
    private TestUnit enemy1;
    private TestUnit enemy2;
    
    @Mock
    private SpriteBatch mockBatch;

    // Classe concrète pour tester la classe abstraite Unit
    private class TestUnit extends Unit {
        public TestUnit(String filePath, float posX, float posY) {
            super(filePath, posX, posY);
            this.health = 100;
            this.attackDamage = 10;
            this.attackSpeed = 5;
            this.speed = 2.0f;
            this.range = 50;
        }

        @Override
        public void move() {
            this.posX += speed;
        }
    }

    @BeforeClass
    public static void init() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        application = new HeadlessApplication(new ApplicationAdapter() {}, config);
        Gdx.gl = mock(GL20.class);
    }

    @AfterClass
    public static void cleanup() {
        if (application != null) {
            application.exit();
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // Le chemin doit être relatif au dossier assets
        unit = new TestUnit("units/hero/down3.png", 100, 200);
        enemy1 = new TestUnit("units/hero/down3.png", 120, 220);
        enemy2 = new TestUnit("units/hero/down3.png", 500, 500);
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
        assertTrue(unit.getSprite() instanceof Sprite);
    }

    @Test
    public void testSetSpritePosX() {
        unit.setSpritePosX(150);
        assertEquals(150, unit.getPosX(), 0.01f);
        assertEquals(150, unit.getSprite().getX(), 0.01f);
    }

    @Test
    public void testSetSpritePosY() {
        unit.setSpritePosY(250);
        assertEquals(250, unit.getPosY(), 0.01f);
        assertEquals(250, unit.getSprite().getY(), 0.01f);
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
    public void testGetLastMove() {
        assertEquals(0.0f, unit.getLastMove(), 0.01f);
    }

    @Test
    public void testSetLastMove() {
        unit.setLastMove(5.5f);
        assertEquals(5.5f, unit.getLastMove(), 0.01f);
    }

    @Test
    public void testSetLastMoveNegative() {
        unit.setLastMove(-2.3f);
        assertEquals(-2.3f, unit.getLastMove(), 0.01f);
    }

    @Test
    public void testSpecialAbility() {
        unit.specialAbility();
    }

    @Test
    public void testOnDeath() {
        unit.takeDamage(100);
        assertTrue(unit.isDead());
    }

    @Test
    public void testMove() {
        float initialX = unit.getPosX();
        unit.move();
        assertEquals(initialX + unit.speed, unit.getPosX(), 0.01f);
    }
}