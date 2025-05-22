package domain;



public abstract class Tower {
    protected int level;
    protected int damage;
    protected double range;
    protected float fireRate;
    protected int cost;
	private int row;
	private int col;

	public Tower(int damage, float fireRate, int cost) {
    	this.level = 1;
    	this.damage = damage;
    	this.fireRate = fireRate;
    	this.cost = cost;
    	this.range = 5;
    }

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public float getFireRate() {
		return fireRate;
	}

	public void setFireRate(float fireRate) {
		this.fireRate = fireRate;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public void fireProjectile(Enemy enemy){
    	Projectile projectile = new Projectile(this, enemy, getDamage());
    	
    }

    public void upgradeTower(){
    	setCost(2*this.cost);
    	setFireRate(1.2f*this.fireRate);
    	setDamage(2*this.damage);
    	setLevel(this.level+1);
    }

    public int sellTower(){
        return getCost()/2;
    }
}
