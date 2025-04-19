package domain;


public class Map {
 private Tile[][] tiles;
 
 public Map(){
   this.tiles=generateMap();
 }
 public static Tile[][] generateMap(){
   Tile[][] tls = new Tile[16][16];
   for(int i=0;i<16;i++){
      for(int j=0;j<16;j++){
         tls[i][j] = new Tile();
      }
   }
   return tls;
}

 public static boolean validateMap(){

    return false;
}

 public static void saveMap(){
     
}
}
