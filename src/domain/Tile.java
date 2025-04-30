package domain;
import java.io.Serializable;
import java.util.ArrayList;

public class Tile implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();
	protected int xCoordinate;
	protected int yCoordinate;
	private TileType type;
	public Tile(TileType type) {
		this.type=type;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}
	public TileType getType() {
		return type;
	}
	public void setType(TileType type) {
		this.type = type;
	}
	public ArrayList<Tile> getAdjacentTiles() {
		return adjacentTiles;
	}
	public void setAdjacentTiles(ArrayList<Tile> adjacentTiles) {
		this.adjacentTiles = adjacentTiles;
	}
	public int getxCoordinate() {
		return xCoordinate;
	}
	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}
	public int getyCoordinate() {
		return yCoordinate;
	}
	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
	

}
