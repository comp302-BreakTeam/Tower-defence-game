package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEngine {
	private Player player;
	private Wave currentWave;
	private List<int[]> path;
	private List<Enemy> activeEnemies;
	

	private int tickCounter = 0;
    private int spawnRate = 30;
    private int waveSize = 10;
    private int waveNumber = 1;
    private int maxWaves = 5;
    private int waveCooldown = 180; 
    private int cooldownCounter = 0;
    private boolean waitingForNextWave = false;
    private boolean gameOver = false;
    private Map<Enemy, Float> moveCooldown = new HashMap<>();
    
    public GameEngine(List<int[]> path, int waveSize, int maxWaves,Player player) {
        this.path = path;
        this.waveSize = waveSize;
        this.currentWave = new Wave(waveSize);
        this.activeEnemies = new ArrayList<>();
        this.maxWaves = maxWaves;
        this.player=player;
    }
    
    public void update() {
    	if (gameOver) { 
    		return;
    	}
    	tickCounter++;
    	
    	if (waitingForNextWave) {
    		cooldownCounter++;
            if (cooldownCounter >= waveCooldown) {
                startNextWave();
            }
            return;
    	}
    	if (!currentWave.isEmpty() && tickCounter % spawnRate == 0) {
            Enemy e = currentWave.nextEnemy();
            e.setPosition(path.get(0)[0], path.get(0)[1]);
            activeEnemies.add(e);
            moveCooldown.put(e, 0f);
        }
    	List<Enemy> toRemove = new ArrayList<>();
    	 for (Enemy e : activeEnemies) {
    		 float cooldown = moveCooldown.get(e);
    		 cooldown += e.getSpeed() / 20f;
    		 if (cooldown < 1f) {
    			    moveCooldown.put(e, cooldown);
    			    continue;
    			}
    		 moveCooldown.put(e, 0f);
    		 int index = getCurrentPathIndex(e);
    		 if (index + 1 < path.size()) {
    			 int[] next = path.get(index + 1);
    			 e.setPosition(next[0], next[1]);
    		 }else {
                 toRemove.add(e);
                 player.reducePlayerLives();
                 if(player.getLives()<=0) {
                	 gameOver=true;
                	 return;
                 }
             }
    		 
    	 }

         activeEnemies.removeAll(toRemove);

        if (isWaveOver() && waveNumber < maxWaves) {
            waitingForNextWave = true;
            cooldownCounter = 0;
        } else if (isWaveOver() && waveNumber >= maxWaves) {
            gameOver = true;
        }
    }
    
    
    
    private int getCurrentPathIndex(Enemy e) {
        for (int i = 0; i < path.size(); i++) {
            int[] pos = path.get(i);
            if (e.getRow() == pos[0] && e.getCol() == pos[1]) {
                return i;
            }
        }
        return 0;
    }
    private void startNextWave() {
        waveNumber++;
        currentWave = new Wave(waveSize + waveNumber);
        waitingForNextWave = false;
    }
    public int getMaxWaves() {
		return maxWaves;
	}

    public List<Enemy> getActiveEnemies() {
        return activeEnemies;
    }

    public boolean isWaveOver() {
        return currentWave.isEmpty() && activeEnemies.isEmpty();
    }
    
    public int getWaveNumber() {
        return waveNumber;
    }

    public boolean isGameOver() {
        return gameOver;
    }

}