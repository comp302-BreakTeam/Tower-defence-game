package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.Goblin;
import domain.Knight;
import javafx.embed.swing.JFXPanel;

public class EnemyTest {
	/**
	 * Overview:
	 * Enemy class represents the enemy units. Each enemy has a health speed and has status flags like isSlowed or combat synergy. 
	 * Enemies move trough the map and take damage from towers and can influence other enemies speed based on position.
	 */
	
	/**
	 * The abstraction function is
	 * AF(e) = an enemy 
	 * with:
	 * 		   position = (e.row, e.col)
	 *         health = e.health
	 *         speed = e.speed
	 *         slowed status = e.isSlowed
	 *         combat synergy = e.hasCombatSynergy
	 *         reward = e.reward
	 */
	/**
	 * The rep invariant is
	 * 		   health ≥ 0 &&
	 * 		   speed > 0 &&
	 * 		   reward ≥ 0 &&
	 * 		   16 > row ≥ 0 && 
	 * 		   16 > col ≥ 0
	 */
	

    private Knight knight;
    
    @BeforeEach
    public void setup() {
        knight = new Knight();
        
    }
    @Test
    public void testValidEnemyPosition() {
        knight.setRow(2);
        knight.setCol(3);
        assertTrue(knight.repOk());
    }
    
    @Test
    public void testInvalidHealth() {
        knight.setHealth(-50);
        assertFalse(knight.repOk());
    }
    
    @Test
    public void testInvalidSpeed() {
        knight.setSpeed(0);
        assertFalse(knight.repOk());
    }
    
    @Test
    public void testValidReward() {
        knight.setReward(100);
        assertTrue(knight.repOk());
    }
    
	
	
}
