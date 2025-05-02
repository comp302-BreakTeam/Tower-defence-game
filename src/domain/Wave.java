package domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Wave {
	public Random rand = new Random(2);
	private Queue<Enemy> enemies = new LinkedList<Enemy>();

	public Wave(int waveSize) {
		int i=0;
		for(i=0; i<waveSize; i++) {
			if(rand.nextBoolean()) {
				enemies.add(new Goblin());
			}
			else {
				enemies.add(new Knight());
			}
		}
		
		
	}
	
	 public Enemy nextEnemy() {
	        return enemies.poll();
	    }

	    public boolean isEmpty() {
	        return enemies.isEmpty();
	    }

	    public int size() {
	        return enemies.size();
	    }
}