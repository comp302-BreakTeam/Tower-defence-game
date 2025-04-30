package domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class Wave {
	private ArrayList<Enemy> enemies = new ArrayList();
	private long spawnInterval;
	private int waveNumber;
	public Wave(ArrayList<Enemy> enemies, long spawnInterval, int waveNumber) {
		super();
		this.enemies = enemies;
		this.spawnInterval = spawnInterval;
		this.waveNumber = waveNumber;
	}
	public void startWave() {
		for(Enemy e:enemies) {
			e.moveForward();
			try {
				wait(spawnInterval);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	public boolean hasNextEnemy() {
		if(!enemies.isEmpty()) {
			return true;
		}
		return false;
		
	}
	public Enemy getNextEnemy() {
		if(hasNextEnemy()) {
			return enemies.get(0);
		}
		return null;
		
	}
	public boolean isFinished() {
		if(!hasNextEnemy()) {
			return true;
		}
		return false;
		
	}
}
