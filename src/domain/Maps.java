package domain;

import java.io.Serializable;

public class Maps implements Serializable {
 private static final long serialVersionUID = 1L; // for save
 
 private Tile[][] mapGrid; 
 private Tile startTile;
 private Tile endTile;
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

 
 public Maps() {
	 this.mapGrid = new Tile[16][16];
	 for(int x=0;x<16;x++) {
		 for(int y =0;y<16;y++) {
			 this.mapGrid[x][y]= new fixEmptyTile(TileType.GRASS); // creates a map full of grass
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
 
 public int[] getCoordinatesof(Tile tile) {
	 int[] coordinates = new int[2];
	 for(int row=0; row<mapGrid.length; row++) {
		 for(int col=0; col<mapGrid[row].length; col++) {
			 if(mapGrid[row][col].equals(tile)) {
				 coordinates[0] = row;
				 coordinates[1] = col;
			 }
		 }
	 }
	return coordinates;
 }

}
