package domain;

import java.io.Serializable;

public class Map implements Serializable {
 private static final long serialVersionUID = 1L;
 
 private Tile[][] mapGrid;
 
 public Map() {
	 this.mapGrid = new Tile[16][16];
	 for(int x=0;x<16;x++) {
		 for(int y =0;y<16;y++) {
			 this.mapGrid[x][y]= new fixEmptyTile(TileType.GRASS);
		 }
	 }
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
