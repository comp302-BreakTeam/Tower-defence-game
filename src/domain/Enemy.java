package domain;

import java.util.List;

public abstract class Enemy {
	protected int health = 100;
	protected float speed;
	protected int reward; // the amount of coins the enemy gives when it dies
	protected int row; 
    protected int col;
    public final int maxHP=100;
    protected double xCoordinate;
    protected double yCoordinate;
    public boolean hasCombatSynergy = false;

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
    public void setPosition(int row, int col) { //set the position of the enmy with the rows and collums of the map
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
    public void takeDamage(double d) { //decrease enemy health
        health -= d;
    }
    public boolean isDead() { // checker for enemy is dead
        return health <= 0;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

     

    public boolean hasEnemyInRange(List<Enemy> enemies, double maxDistance, Class<? extends Enemy> enemyType) {
        for (Enemy enemy : enemies) { //checks each enemy in the given active enemy class 
            if (enemy == this) continue;

            double dx = this.xCoordinate - enemy.xCoordinate;
            double dy = this.yCoordinate - enemy.yCoordinate;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= maxDistance && enemyType.isInstance(enemy)) { // if the distance is lower then the max distance and enemy type is accurate it returns true
                return true;
            }
        }
        return false;
    }

    public float calculateAverageSpeed(float speed1, float speed2) {
        return (speed1 + speed2) / 2.0f;
    }

    public void updateCombatSynergy(List<Enemy> enemies, double tileWidth) {
        if (this instanceof Knight) {
            if (hasEnemyInRange(enemies, tileWidth, Goblin.class)) {
                float averageSpeed = calculateAverageSpeed(1.0f, 1.2f); //Knight's speed (1.0) and Goblin's speed (1.2)
                this.speed = averageSpeed;
                this.hasCombatSynergy = true;
            } else {
                this.speed = 1.0f; //Reverting to original speed
                this.hasCombatSynergy = false;
            }
        }
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


	

}
