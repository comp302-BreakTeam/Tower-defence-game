package domain;
import java.io.Serializable;

public class Tile implements Serializable {
	private static final long serialVersionUID = 1L;
	private TileType type;
	public Tile(TileType type) {
		this.type=type;
	}
	public TileType getType() {
		return type;
	}
	public void setType(TileType type) {
		this.type = type;
	}
	

}
