package test;

import static org.junit.jupiter.api.Assertions.*;

import domain.*;
import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.ProjectileController;

import java.util.ArrayList;
import java.util.List;

public class FireFromTowerTest {

    private TestGameEngine engine;
    private TestMapGrid mapGrid;
    private List<ProjectileController> activeProjectiles;
    private int startrow = 3;
    private int startcol = 5;

    @BeforeEach
    public void setUp() {
        // Needed to initialize JavaFX toolkit
        new JFXPanel(); 

        engine = new TestGameEngine();
        mapGrid = new TestMapGrid();
        activeProjectiles = new ArrayList<>();
    }

    @Test
    public void testFireWithArcherTower_usesArrowImage() {
        Tower tower = new Archer_Tower();
        Enemy target = new DummyEnemy();

        FireTester tester = new FireTester(engine, mapGrid, activeProjectiles, startrow, startcol);
        tester.fireFromTower(tower, target);

        ProjectileController controller = activeProjectiles.get(0);
        Image[] frames = controller.getFrames();  
        assertEquals(1, frames.length);
        assertTrue(frames[0].impl_getUrl().contains("arrow.png"));
    }

    @Test
    public void testFireWithMageTowerLevel2_usesBlueFireball() {
        Tower tower = new Mage_Tower();
        tower.upgradeTower(); // now level = 2
        Enemy target = new DummyEnemy();

        FireTester tester = new FireTester(engine, mapGrid, activeProjectiles, startrow, startcol);
        tester.fireFromTower(tower, target);

        ProjectileController controller = activeProjectiles.get(0);
        Image[] frames = controller.getFrames();
        assertEquals(6, frames.length);
        assertTrue(frames[0].impl_getUrl().contains("blue_fireball"));
    }

    @Test
    public void testFireWithArtilleryTower_usesBombImage() {
        Tower tower = new Artillery_Tower();
        Enemy target = new DummyEnemy();

        FireTester tester = new FireTester(engine, mapGrid, activeProjectiles, startrow, startcol);
        tester.fireFromTower(tower, target);

        ProjectileController controller = activeProjectiles.get(0);
        Image[] frames = controller.getFrames();
        assertEquals(1, frames.length);
        assertTrue(frames[0].impl_getUrl().contains("bomb.png"));
    }
}
