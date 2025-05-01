package domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class Wave {
	private ArrayList<Enemy> enemies = new ArrayList();
	private long spawnInterval;
	private int waveSize;
	private float enemySpeed;
	public Wave(long spawnInterval, int waveSize) {
		super();
		int i=0;
		for(i=0; i<waveSize/2; i++) {
			enemies.set(i, new Goblin());
		}for(int k=i; k<waveSize; k++) {
			enemies.set(i, new Knight());
		}
		i=0;
		this.spawnInterval = spawnInterval;
		this.waveSize = waveSize;
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
