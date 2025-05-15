package domain;

public class fixEmptyTile extends Tile{
	
	private static final long serialVersionUID = 1L; //for save
	final private Tower tower = null;
	final private Enemy enemy = null;
	public fixEmptyTile(TileType type) {
		super(type); //sets the type of the empty tile can be grass rock tree
		
	}
	public Tower getTower() {
		return tower;
	}
	public Enemy getEnemy() {
		return enemy;
	}
}
