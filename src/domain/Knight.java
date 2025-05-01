package domain;

import java.util.ArrayList;

public class Knight extends Enemy{
	protected int health = 100;
	protected float speed = (float) 1.0;
	protected int coin;
	protected pathTile position;
	
	public Knight() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void removeEnemy(Enemy enemy) {
		// TODO Auto-generated method stub
		
	}public void damaged() {
		if (this.health>0){
			health = health - 1;
		}if(health<=0) {
			removeEnemy(this);
		}
	}

	public void moveForward() {
		for(pathTile tile : sort(position.getAdjacentTiles())) {
			if(tile.getEnemyOnTile()!=null) {
				continue;
			}tile.setEnemyOnTile(this);
			this.position.setEnemyOnTile(null);
			setPosition(tile);
			tile.setEnemyOnTile(this);
		}
	}

	private ArrayList<pathTile> sort(ArrayList<Tile> adjacentTiles) {
	    ArrayList<pathTile> pathTiles = new ArrayList<>();
	    for (Tile tile : adjacentTiles) {
	        if (tile.getType() == TileType.PATH) { 
	            pathTiles.add((pathTile) tile);
	        }
	    }
	    pathTiles.sort((t1, t2) -> {
	        if (t1.getyCoordinate() != t2.getyCoordinate()) {
	            return Integer.compare(t1.getyCoordinate(), t2.getyCoordinate());
	        } else {
	            return Integer.compare(t1.getxCoordinate(), t2.getxCoordinate());
	        }
	    });
	    
	    return pathTiles;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public pathTile getPosition() {
		return position;
	}

	public void setPosition(pathTile position) {
		this.position = position;
	}
 


} 


