package test.java.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import domain.Enemy;
import domain.Knight;
import domain.Goblin;

public class CombatSynergyTestergyTest {
    private Knight knight;
    private Goblin goblin;
    private List<Enemy> enemies;
    private static final double TILE_WIDTH = 75.0;

    @BeforeEach
    void setUp() {
        knight = new Knight();
        goblin = new Goblin();
        enemies = new ArrayList<>();
    }

    @Test
    void testRequiresClause() {
        // Only test for NullPointerException when enemies is null
        assertThrows(NullPointerException.class, () -> {
            knight.updateCombatSynergy(null, TILE_WIDTH);
        });
    }

    @Test
    void testModifiesClause() {
        // Set up Knight and Goblin in range so speed and synergy will change
        knight.setPosition(0, 0);
        goblin.setPosition(0, 1); // In range
        enemies.add(goblin);

        float initialSpeed = knight.getSpeed();
        boolean initialSynergy = knight.hasCombatSynergy;

        knight.updateCombatSynergy(enemies, TILE_WIDTH);

        assertNotEquals(initialSpeed, knight.getSpeed());
        assertNotEquals(initialSynergy, knight.hasCombatSynergy);
    }

    @Test
    void testEffectsClause() {
        // Test effects: If Knight and Goblin in range, speed = average and synergy = true
        // Otherwise, speed = 1.0 and synergy = false
        
        // Case 1: Knight and Goblin in range
        knight.setPosition(0, 0);
        goblin.setPosition(0, 1);
        enemies.add(goblin);
        knight.updateCombatSynergy(enemies, TILE_WIDTH);
        assertEquals(1.1f, knight.getSpeed(), 0.001f);
        assertTrue(knight.hasCombatSynergy);
        
        // Case 2: Knight and Goblin out of range
        knight.setPosition(0, 0);
        goblin.setPosition(0, 2);
        knight.updateCombatSynergy(enemies, TILE_WIDTH);
        assertEquals(1.0f, knight.getSpeed(), 0.001f);
        assertFalse(knight.hasCombatSynergy);
    }
} 