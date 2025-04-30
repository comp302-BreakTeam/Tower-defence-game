package domain;

public class towerTile extends Tile{
	private static final long serialVersionUID = 1L;
	private Tower tower = null;
	final private Enemy enemy = null;
	
	public towerTile(TileType grass) {
		super(TileType.EMPTY_PLOT);
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
