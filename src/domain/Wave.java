package domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Wave {
	private Queue<Enemy> enemies = new LinkedList<Enemy>();
	private int waveSize;
	public Wave(int waveSize) {
		int i=0;
		for(i=0; i<waveSize/2; i++) {
			enemies.add(new Goblin());
		}for(int k=i; k<waveSize; k++) {
			enemies.add(new Knight());
		}
		i=0;
		this.waveSize = waveSize;
	}
	public void startWave() {
		for(Enemy e:enemies) {
			e.moveForward();
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
			return enemies.poll();
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
