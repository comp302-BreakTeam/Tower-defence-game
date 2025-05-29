package test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import domain.Player;

public class PlayerTest {

    @Test
    public void testCanAffordSuccess() {
        Player player = new Player("TestPlayer", 3, 100);
        assertTrue(player.canAfford(40));  
        assertEquals(60, player.getGold()); 
    }

    @Test
    public void testCanAffordFail() {
        Player player = new Player("TestPlayer", 3, 30);
        assertFalse(player.canAfford(50)); 
        assertEquals(30, player.getGold());  
    }

    @Test
    public void testCanAffordExactAmount() {
        Player player = new Player("TestPlayer", 3, 75);
        assertTrue(player.canAfford(75));  
        assertEquals(0, player.getGold());  
    }
}
