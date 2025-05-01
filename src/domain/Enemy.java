package domain;

import java.util.ArrayList;

public abstract class Enemy {
	protected int health = 100;
	protected float speed;
	protected int reward;
	protected int row;
    protected int col;
    
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
	
    public Enemy(int health, float speed, int reward) {
        this.health = health;
        this.speed = speed;
        this.reward = reward;
    }
    public int getRow() { 
    	return row; 
    	}
    public int getCol() { 
    	return col; 
    	}
    public int getReward() {
    	return reward;
    }
    public void takeDamage(int damage) {
        health -= damage;
    }
    public boolean isDead() {
        return health <= 0;
    }
	
	

	

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	

}