package domain;



public class Player {
	private String name;
    private int gold;
	private int lives;
	private int maxWave;
	private int waveSize;
    
    public Player(String name, int lives, int gold) {
		this.name= name;
		this.lives= lives;
		this.gold= gold;
		this.waveSize= 10;
		this.maxWave = 5;
	}
    
    public void setGold(int gold) {
    	this.gold = gold;
    }
    
    public void setLives(int lives) {
    	this.lives = lives;
    }
    
	public int getMaxWave() {
		return maxWave;
	}

	public void setMaxWave(int maxWave) {
		this.maxWave = maxWave;
	}

	public int getWaveSize() {
		return waveSize;
	}

	public void setWaveSize(int waveSize) {
		this.waveSize = waveSize;
	}

	public void reducePlayerLives(){
    	--lives;
    
    }
    
    public void addGold(int amount) {
    	gold += amount;
    }

    public boolean canAfford(int amount){
    	if(amount<= gold) {
    		deductGold(amount);
    		return true;
    	}
        return false;
    }

    public void deductGold(int amount){
    	gold -= amount;
        
    }
    public int getGold() {
		return gold;
	}


	public int getLives() {
		return lives;
	}


	public String getName() {
		return name;
	}

}
