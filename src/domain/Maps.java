package domain;

import java.io.Serializable;

public class Maps implements Serializable {
 private static final long serialVersionUID = 1L;
 
 private Tile[][] mapGrid;
 private Tile startTile;
 public Tile getStartTile() {
	return startTile;
}

public void setStartTile(Tile startTile) {
	this.startTile = startTile;
}

public Tile getEndTile() {
	return endTile;
}

public void setEndTile(Tile endTile) {
	this.endTile = endTile;
}
private Tile endTile;
 
 public Maps() {
	 this.mapGrid = new Tile[16][16];
	 for(int x=0;x<16;x++) {
		 for(int y =0;y<16;y++) {
			 this.mapGrid[x][y]= new fixEmptyTile(TileType.GRASS);
		 }
	 }
	 this.startTile = null;
	 this.endTile=null;
 }

 public Tile[][] getMapGrid() {
	return mapGrid;
	
 }
 
 public Tile getTile(int row,int col) {
	 return mapGrid[row][col];
 }
 public void setTile(int row,int col,Tile tile) {
	 mapGrid[row][col]=tile;
 }
 
 

}
