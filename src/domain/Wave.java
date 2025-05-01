package domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Wave {
	private Queue<Enemy> enemies = new LinkedList<Enemy>();

	public Wave(int waveSize) {
		int i=0;
		for(i=0; i<waveSize/2; i++) {
			enemies.add(new Goblin());
		}for(int k=i; k<waveSize; k++) {
			enemies.add(new Knight());
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