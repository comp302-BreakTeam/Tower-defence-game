package domain;



public class Player {
	private String name;
    private int gold;
	private int lives;
    
    public Player(String name, int lives, int gold) {
		this.name= name;
		this.lives= lives;
		this.gold= gold;
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
