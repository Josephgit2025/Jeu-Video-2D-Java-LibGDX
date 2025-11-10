package com.main.map;

import com.main.GameScreen;
import com.main.entities.Unit;
import com.main.utils.Position;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseTest {

    private Base base;
    
    @Mock
    private GameScreen mockScreen;
    
    @Mock
    private Unit mockUnit;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        base = new Base(100, 200);
        when(mockScreen.getMapHeight()).thenReturn(800);
    }

    @Test
    public void testConstructor() {
        assertNotNull(base);
        assertEquals(1000, base.getHealth());
        assertNotNull(base.getPosition());
        assertEquals(100, base.getPosition().x);
        assertEquals(200, base.getPosition().y);
        assertEquals(50, base.getAttackPower());
        assertNotNull(base.getUnits());
        assertTrue(base.getUnits().isEmpty());
    }

    @Test
    public void testGetHealth() {
        assertEquals(1000, base.getHealth());
    }

    @Test
    public void testGetPosition() {
        Position pos = base.getPosition();
        assertNotNull(pos);
        assertEquals(100, pos.x);
        assertEquals(200, pos.y);
    }

    @Test
    public void testGetAttackPower() {
        assertEquals(50, base.getAttackPower());
    }

    @Test
    public void testTakeDamage() {
        base.takeDamage(100);
        assertEquals(900, base.getHealth());
        
        base.takeDamage(200);
        assertEquals(700, base.getHealth());
    }

    @Test
    public void testTakeDamageToZero() {
        base.takeDamage(1000);
        assertEquals(0, base.getHealth());
    }

    @Test
    public void testTakeDamageNegative() {
        base.takeDamage(1500);
        assertEquals(-500, base.getHealth());
    }

    @Test
    public void testAddUnit() {
        assertEquals(0, base.getUnits().size());
        
        base.addUnit(mockUnit);
        assertEquals(1, base.getUnits().size());
        assertTrue(base.getUnits().contains(mockUnit));
    }

    @Test
    public void testAddNullUnit() {
        base.addUnit(null);
        assertEquals(0, base.getUnits().size());
    }

    @Test
    public void testAddMultipleUnits() {
        Unit mockUnit2 = mock(Unit.class);
        
        base.addUnit(mockUnit);
        base.addUnit(mockUnit2);
        
        assertEquals(2, base.getUnits().size());
    }

    @Test
    public void testGetUnits() {
        assertTrue(base.getUnits().isEmpty());
        
        base.addUnit(mockUnit);
        assertEquals(1, base.getUnits().size());
    }

    @Test
    public void testSpawnUnitBeforeTime() {
        Unit unit = base.spawnUnit(mockScreen, 2.0f);
        assertNull(unit);
        
        unit = base.spawnUnit(mockScreen, 2.0f);
        assertNull(unit);
    }

    @Test
    public void testSpawnUnitAfterTime() {
        base.spawnUnit(mockScreen, 5.0f);
        Unit unit = base.spawnUnit(mockScreen, 0.1f);
        assertNotNull(unit);
    }

    @Test
    public void testSpawnUnitResetsTimer() {
        base.spawnUnit(mockScreen, 5.0f);
        Unit firstUnit = base.spawnUnit(mockScreen, 0.1f);
        assertNotNull(firstUnit);
        
        Unit secondUnit = base.spawnUnit(mockScreen, 2.0f);
        assertNull(secondUnit);
    }

    @Test
    public void testSpawnUnitMultipleTimes() {
        for (int i = 0; i < 10; i++) {
            base.spawnUnit(mockScreen, 5.1f);
            Unit unit = base.spawnUnit(mockScreen, 0.1f);
            if (unit != null) {
                assertNotNull(unit);
            }
        }
    }

    @Test
    public void testUpdateUnitsWithNoUnits() {
        base.updateUnits(0.5f);
        assertEquals(0, base.getUnits().size());
    }

    @Test
    public void testUpdateUnitsBeforeMove() {
        when(mockUnit.getLastMove()).thenReturn(0.5f);
        base.addUnit(mockUnit);
        
        base.updateUnits(0.3f);
        
        verify(mockUnit, times(1)).getLastMove();
        verify(mockUnit, times(1)).setLastMove(0.8f);
        verify(mockUnit, never()).move();
    }

    @Test
    public void testUpdateUnitsAfterMove() {
        when(mockUnit.getLastMove()).thenReturn(1.0f);
        base.addUnit(mockUnit);
        
        base.updateUnits(0.5f);
        
        verify(mockUnit, times(1)).move();
        verify(mockUnit, times(1)).setLastMove(0);
    }

    @Test
    public void testUpdateUnitsMultipleUnits() {
        Unit mockUnit2 = mock(Unit.class);
        when(mockUnit.getLastMove()).thenReturn(1.0f);
        when(mockUnit2.getLastMove()).thenReturn(0.5f);
        
        base.addUnit(mockUnit);
        base.addUnit(mockUnit2);
        
        base.updateUnits(0.3f);
        
        verify(mockUnit, times(1)).move();
        verify(mockUnit, times(1)).setLastMove(0);
        verify(mockUnit2, times(1)).setLastMove(0.8f);
        verify(mockUnit2, never()).move();
    }

    @Test
    public void testUpdateUnitsExactlyAtThreshold() {
        when(mockUnit.getLastMove()).thenReturn(1.0f);
        base.addUnit(mockUnit);
        
        base.updateUnits(0.0f);
        
        verify(mockUnit, times(1)).move();
        verify(mockUnit, times(1)).setLastMove(0);
    }
}