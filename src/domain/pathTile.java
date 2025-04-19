package domain;

import java.util.ArrayList;

public class pathTile extends Tile{
	private ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();
	private Enemy enemyOnTile = null;
	final private Tower tower = null;

	public pathTile(ArrayList<Tile> adjacentTiles, Enemy enemyOnTile) {
		super();
		this.adjacentTiles = adjacentTiles;
		this.enemyOnTile = enemyOnTile;
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
