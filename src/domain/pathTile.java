package domain;

import java.util.ArrayList;

public class pathTile extends Tile{
	private static final long serialVersionUID = 1L;
	private ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();
	private Enemy enemyOnTile = null;
	final private Tower tower = null;

	public pathTile() {
		super(TileType.PATH);
		
	}

	public ArrayList<Tile> getAdjacentTiles() {
		return adjacentTiles;
	}

	public void setAdjacentTiles(ArrayList<Tile> adjacentTiles) {
		this.adjacentTiles = adjacentTiles;
	}

	public Enemy getEnemyOnTile() {
		return enemyOnTile;
	}

	public void setEnemyOnTile(Enemy enemyOnTile) {
		this.enemyOnTile = enemyOnTile;
	}
	
	public Tower getTower() {
		return tower;
	}
	
}
