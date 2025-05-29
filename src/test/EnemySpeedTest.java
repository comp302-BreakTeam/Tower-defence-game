package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.Knight;
import javafx.embed.swing.JFXPanel;

public class EnemySpeedTest {
	private Knight knight;
	
	 @BeforeEach
	    public void setup() {
	        new JFXPanel();
	        knight = new Knight(); 
	 }
	@Test
    public void testBeSlowedReducesSpeed() {
		
		float originalSpeed = knight.getSpeed();
		knight.beSlowed();
		assertEquals(originalSpeed * 0.8f, knight.getSpeed(), 0.01f);
	}
	
	@Test
    public void testBeSlowedSetsSlowedFlag() {
		
		knight.beSlowed();
		assertTrue(knight.isSlowed());
	}
	
	@Test
    public void testSpeedRestoresAfterDelay() throws InterruptedException {
		
		float originalSpeed = knight.getSpeed();
		knight.beSlowed();
		Thread.sleep(4500);
		assertFalse(knight.isSlowed());
		assertEquals(originalSpeed, knight.getSpeed(), 0.01f);
	}
	

}
