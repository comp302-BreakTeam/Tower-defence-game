package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Iterator;
import domain.GoldBag;

public class GameEngine {
	private Player player;
	private Wave currentWave;
	private List<int[]> path; // list of path coordinates
	private List<Enemy> activeEnemies;	
	private int tickCounter = 0; 
    private int spawnRate = 30;// every 30 tick enemy spawns
    private int waveSize = 10;
    private int waveNumber = 1;
    private int maxWaves = 5;
    private int waveCooldown = 180;  // after wave there is 180 tick time till next
    private int cooldownCounter = 0;
    private boolean waitingForNextWave = false;
    private boolean gameOver = false;
    private Map<Enemy, Float> moveCooldown = new HashMap<>();  //maps each enemy to its move cooldown
    private List<GoldBag> goldBags = new ArrayList<>();
    private Random random = new Random();
    
    public GameEngine(List<int[]> path, int waveSize, int maxWaves,Player player) {
        this.path = path; 
        this.waveSize = waveSize;
        this.currentWave = new Wave(waveSize);
        this.activeEnemies = new ArrayList<>();
        this.maxWaves = maxWaves;
        this.player=player;
    }
    
    public void update() {
    	if (gameOver) { // if game over it returns 
    		return;
    	}
    	tickCounter++; // increment the tick
    	
    	if (waitingForNextWave) { // if waiting for wave it increments the cooldown
    		cooldownCounter++;
            if (cooldownCounter >= waveCooldown) { // it starts the next wave if cooldown is finished
                startNextWave();
            }
            return;
    	}
    	if (!currentWave.isEmpty() && tickCounter % spawnRate == 0) { // if the wave is not empty and it is in spawn rate tick it gets a new enemy form wave
            Enemy e = currentWave.nextEnemy();
            e.setPosition(path.get(0)[0], path.get(0)[1]); //sets enemy pos to start tile
            activeEnemies.add(e); // add enemy to active enemy
            moveCooldown.put(e, 0f); // maps its move cooldown
        }
    	List<Enemy> toRemove = new ArrayList<>(); //enemies that reached the end 
    	 for (Enemy e : activeEnemies) {
             e.updateCombatSynergy(activeEnemies, 75.0); // 75.0 is the tile width
    		 float cooldown = moveCooldown.get(e);
    		 cooldown += e.getSpeed() / 20f; //move cooldown is related to enemy speed
    		 if (cooldown < 1f) { 
    			    moveCooldown.put(e, cooldown); 
    			    continue; // if cooldown is not reached increases the cooldown and the enemy does not move
    			}
    		 moveCooldown.put(e, 0f); // sets the new move cooldown to zero 
    		 int index = getCurrentPathIndex(e);
    		 if (index + 1 < path.size()) {//checks if enemy reached end if not enemy goes to next path
    			 int[] next = path.get(index + 1);
    			 e.setPosition(next[0], next[1]);
    		 }else {
                 toRemove.add(e);
                 player.reducePlayerLives(); // decreases the player life if enemy reached the end
                 if(player.getLives()<=0) {
                	 gameOver=true; // if player has no life game ends 
                	 return;
                 }
             }
    		 
    	 }

         activeEnemies.removeAll(toRemove); // removes the enemy that reached the end

        if (isWaveOver() && waveNumber < maxWaves) {
            waitingForNextWave = true; //checks if max wave is reached
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

    public List<GoldBag> getGoldBags() {
        return goldBags;
    }

    public void maybeDropGoldBag(Enemy e, int archerTowerCost) {
        if (e.getHealth() <= 0) {
            double dropChance = 0.5;
            if (random.nextDouble() < dropChance) {
                int minGold = 2;
                int maxGold = archerTowerCost / 2;
                int amount = minGold + random.nextInt(Math.max(1, maxGold - minGold + 1));
                goldBags.add(new GoldBag(e.getxCoordinate(), e.getyCoordinate(), amount));
            }
        }
    }

    public void removeGoldBag(GoldBag bag) {
        goldBags.remove(bag);
    }
}