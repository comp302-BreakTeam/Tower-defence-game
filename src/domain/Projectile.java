package domain;



public class Projectile {
	private Tower source;
	private Enemy target;
	private int damage;
	
	public Tower getSource() {
		return source;
	}


	public void setSource(Tower source) {
		this.source = source;
	}


	public Enemy getTarget() {
		return target;
	}


	public void setTarget(Enemy target) {
		this.target = target;
	}


	public int getDamage() {
		return damage;
	}


	public void setDamage(int damage) {
		this.damage = damage;
	}


	public Projectile(Tower source, Enemy target, int damage) {
		this.source = source;
		this.target = target;
		this.damage = damage;
	}
	
	public void checkCollision() {
		
	}
	
	public void getCoordinates() {
		
	}
	
	public void setDirection(float angle) {
		
	}
	
	public void applyGravity() {
		
	}
}
