package domain;

public class fixEmptyTile extends Tile{
	
	private static final long serialVersionUID = 1L;
	final private Tower tower = null;
	final private Enemy enemy = null;
	public fixEmptyTile(TileType type) {
		super(type);
		
	}
	public Tower getTower() {
		return tower;
	}
	public Enemy getEnemy() {
		return enemy;
	}
}
