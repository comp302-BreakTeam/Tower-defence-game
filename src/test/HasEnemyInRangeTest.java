package test;

import domain.Enemy;
import domain.Goblin;
import domain.Knight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HasEnemyInRangeTest {

    Enemy self;
    Enemy closeGoblin;
    Enemy farGoblin;
    Enemy closeKnight;
    List<Enemy> enemies;

    @BeforeEach
    public void setUp() {
        self = new Goblin(100, 1.0f, 10);
        self.setPosition(0, 0); 
        closeGoblin = new Goblin(100, 1.0f, 10);
        closeGoblin.setPosition(0, 1); 
        farGoblin = new Goblin(100, 1.0f, 10);
        farGoblin.setPosition(0, 10);   
        closeKnight = new Knight(100, 1.0f, 10);
        closeKnight.setPosition(1, 0); 

        enemies = new ArrayList<>();
        enemies.add(self); // self should be ignored
        enemies.add(closeGoblin);
        enemies.add(farGoblin);
        enemies.add(closeKnight);
    }

    @Test
    public void testGoblinInRange_returnsTrue() {
        assertTrue(self.hasEnemyInRange(enemies, 100, Goblin.class));
    }

    @Test
    public void testOnlyFarGoblin_returnsFalse() {
        List<Enemy> onlyFar = new ArrayList<>();
        onlyFar.add(farGoblin);
        assertFalse(self.hasEnemyInRange(onlyFar, 100, Goblin.class));
    }

    @Test
    public void testWrongTypeOnly_returnsFalse() {
        enemies.remove(closeGoblin);
        enemies.remove(farGoblin);
        assertFalse(self.hasEnemyInRange(enemies, 100, Goblin.class)); 
    @Test
    public void testKnightInRange_returnsTrue() {
        assertTrue(self.hasEnemyInRange(enemies, 100, Knight.class));
    }
}
