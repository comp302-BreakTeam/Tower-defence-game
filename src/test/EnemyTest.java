package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import domain.Knight;

public class EnemyTest {
	private Knight knight;
	
	
	@Test
    public void testBeSlowedReducesSpeed() {
		knight = new Knight();
		float originalSpeed = knight.getSpeed();
		knight.beSlowed();
		assertEquals(originalSpeed * 0.8f, knight.getSpeed(), 0.01f);
	}
	
	@Test
    public void testBeSlowedSetsSlowedFlag() {
		knight = new Knight();
		knight.beSlowed();
		assertTrue(knight.isSlowed());
	}
	
	@Test
    public void testSpeedRestoresAfterDelay() throws InterruptedException {
		knight = new Knight();
		float originalSpeed = knight.getSpeed();
		knight.beSlowed();
		Thread.sleep(4500);
		assertFalse(knight.isSlowed());
	}
	

}
