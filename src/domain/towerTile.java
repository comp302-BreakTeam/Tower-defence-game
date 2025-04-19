package domain;

public class towerTile extends Tile{
	private Tower tower = null;
	final private Enemy enemy = null;
	
	public towerTile(Tower tower) {
		super();
		this.tower = tower;
	}
	public boolean isEmpty(towerTile twrTile) {
		if(twrTile.tower==null) {
			return true;
		}return false;
	}
	public Tower getTower() {
		return tower;
	}
	public void setTower(Tower tower) {
		this.tower = tower;
	}
	public Enemy getEnemy() {
		return enemy;
	}
	
}
