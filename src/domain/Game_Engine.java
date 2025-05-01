package domain;

import java.util.ArrayList;

public class Game_Engine {
    boolean isPaused;
    private int waveSize;

    private ArrayList<int[]> path = new ArrayList<int[]>();
    private Wave currentWave;

    public Game_Engine(int waveSize, ArrayList<int[]> path) {
		this.waveSize = waveSize;
		this.path = path;
		currentWave = new Wave(waveSize);
		
	}

	public static void startGame(){
        
    }
    
    public static void pauseGame(){

    }

    public static void resumeGame(){

    }

    public static void checkGameOver(){

    }

    public static void gameOver(){

    }

    public static void spawnEnemy(){

    }

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

	public int getWaveSize() {
		return waveSize;
	}

	public void setWaveSize(int waveSize) {
		this.waveSize = waveSize;
	}

	public ArrayList<int[]> getPath() {
		return path;
	}

	public void setPath(ArrayList<int[]> path) {
		this.path = path;
	}

	public Wave getCurrentWave() {
		return currentWave;
	}

	public void setCurrentWave(Wave currentWave) {
		this.currentWave = currentWave;
	}

}
