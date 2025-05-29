package test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import domain.Archer_Tower;
import domain.Tower;

public class UpgradeTowerTest {
    Tower tower;

    @BeforeEach
    public void setUp() {
        tower = new Archer_Tower();
        //Damage of Archer Tower 10
        //Fire Rate of Archer Tower 6f
        //Cost of Archer Tower 100
    }
    
    @Test
    public void testInitialLevel() {
    	assertEquals(1, tower.getLevel());
    }
    
    @Test
    public void testUpgradeFireRate() {
        tower.upgradeTower();
        assertEquals(12f, tower.getFireRate(), 0.0001);
    }

    @Test
    public void testUpgradeLevel() {
        tower.upgradeTower();
        assertEquals(2, tower.getLevel());
    }

    @Test
    public void testUpgradeRange() {
        tower.upgradeTower();
        assertEquals(7.5f, tower.getRange(), 0.0001);
    }
}

