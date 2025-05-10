package domain;

public abstract class Enemy {
	protected int health = 100;
	protected float speed;
	protected int reward;
	protected int row;
    protected int col;
    public final int maxHP=100;
    protected double xCoordinate;
    protected double yCoordinate;

    public double getxCoordinate() {
        return xCoordinate;
    }
    public double getyCoordinate() {
        return yCoordinate;
    }
    public void setxCoordinate(double xPos) {
        this.xCoordinate = xPos;
    }
    public void setyCoordinate(double yPos) {
        this.yCoordinate = yPos;
    }
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
        this.xCoordinate = col * 75; // tile width
        this.yCoordinate = row * 50; // tile height
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
    public void takeDamage(double d) {
        health -= d;
    }
    public boolean isDead() {
        return health <= 0;
    }
	
	

	

	public void setRow(int row) {
		this.row = row;
	}
	public void setCol(int col) {
		this.col = col;
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
