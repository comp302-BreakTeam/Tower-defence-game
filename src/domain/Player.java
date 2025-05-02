package domain;



public class Player {
	private String name;
    private int gold;
	private int lives;
	private int wave;
    
    public Player(String name, int lives, int gold) {
		this.name= name;
		this.lives= lives;
		this.gold= gold;
		this.wave= 1;
	}
    
    public void setLives(int lives) {
    	this.lives = lives;
    }
    
    public int getWave() {
		return wave;
	}


	public void setWave(int wave) {
		this.wave = wave;
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
