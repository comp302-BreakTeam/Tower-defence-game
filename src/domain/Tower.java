package domain;



public abstract class Tower {
    protected int level;
    protected int damage;
    protected int range;
    protected float fireRate;
    protected int cost;
    
    public Tower(int damage, float fireRate, int cost) {
    	this.level = 1;
    	this.damage = damage;
    	this.fireRate = fireRate;
    	this.cost = cost;
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
        return getCost();
    }
}
