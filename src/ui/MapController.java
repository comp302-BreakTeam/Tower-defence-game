package ui;

import java.io.IOException;

import domain.Maps;
import domain.SaveMap;
import domain.TileType;
import domain.fixEmptyTile;
import domain.pathTile;
import domain.towerTile;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MapController {
	private Scene previousScene;
	private static final int ROWS = 16;
    private static final int COLS = 16;
    private static final int TILE_WIDTH = 75;
    private static final int TILE_HEIGHT = 50;
    private TileType[][] currentMap = new TileType[ROWS][COLS];
    
    @FXML
    private ComboBox<String> tileSelector;
    
    @FXML
    private GridPane mapGrid;
    @FXML
    private Button saveButton;
    @FXML
	private Button back;
    
    @FXML
	private void handleSave() throws IOException {// saves the map but it first checks if the map is valid using validate 
    	if (!validateMap()) {
            System.out.println("Maps validation failed. Cannot save.");
            Alert alert= new Alert(AlertType.ERROR,"Maps validation failed. Cannot save.");
    		alert.showAndWait();
            return; 
        }
    	TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Maps");
        dialog.setHeaderText("Enter a name for your map:");
        dialog.setContentText("Maps Name:");
        java.util.Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent()) {
            String mapName = result.get().trim();
            
            if (mapName.isEmpty()) {
                System.out.println("Maps name can't be empty.");
                Alert alert= new Alert(AlertType.ERROR,"Maps name can't be empty");
        		alert.showAndWait();
                return;
            }
			Maps map = new Maps();
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					TileType type = currentMap[row][col];
					switch (type) {
					case PATH: 
						map.setTile(row, col, new pathTile());
						if(isEdge(row, col)) {
							if(map.getStartTile()==null) {
								map.setStartTile(map.getTile(row, col));
							}
							else {
								map.setEndTile(map.getTile(row, col));
							}
						}
						break;
					case EMPTY_PLOT:
						map.setTile(row, col, new fixEmptyTile(TileType.EMPTY_PLOT));
						break;
					case GRASS:
						map.setTile(row, col, new fixEmptyTile(TileType.GRASS)); 
						break;
					case ROCK:
						map.setTile(row, col, new fixEmptyTile(TileType.ROCK));
						break;
					case TREE:
						map.setTile(row, col, new fixEmptyTile(TileType.TREE));
						break;
						
					
					}
					
				}
			}
			try {
	            SaveMap.saveMap(map, mapName + ".dat"); 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			Stage stage = (Stage)saveButton.getScene().getWindow();
			stage.setScene(previousScene); 
			stage.setTitle("Main Menu");
	        stage.show();
	        
        }
        
	}
    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
    
    @FXML
    private void  initialize() {
    	tileSelector.getItems().addAll("PATH", "GRASS", "EMPTY_PLOT","ROCK","TREE");
    	tileSelector.setValue("PATH");
    	for (int row = 0; row < ROWS; row++) {
    		for (int col = 0; col < COLS; col++) {
    			Button tile = new Button();
    			tile.setPrefSize(TILE_WIDTH, TILE_HEIGHT);
    			setTileBackground(tile, TileType.GRASS);
    			currentMap[row][col] = TileType.GRASS;
    			final int r = row;
                final int c = col;
    			tile.setOnAction(e->{
    				TileType selected = TileType.valueOf(tileSelector.getValue());
    				setTileBackground(tile, selected);
    				currentMap[r][c] = selected;
    			});
    			mapGrid.add(tile, col, row);
    		}
    	}
    }
    public void setTileBackground(Button tile, TileType type) {
        String imagePath = "assets/grass.png"; 

        switch (type) {
            case PATH:
                imagePath = "assets/path.png";
                break;
            case GRASS:
                imagePath = "assets/grass.png";
                break;
            case ROCK:
                imagePath = "assets/rock.png";
                break;
            case TREE:
                imagePath = "assets/tree.png";
                break;
            case EMPTY_PLOT:
                imagePath = "assets/empty_plot.png";
                break;
            default:
                imagePath = "assets/grass.png";
                break;
        }

        tile.setStyle(
            "-fx-background-image: url('" + getClass().getResource(imagePath) + "');" +
            "-fx-background-size: cover;"
        );
    }
    private boolean isEdge(int row, int col) {
        return row == 0 || row == ROWS - 1 || col == 0 || col == COLS - 1;
    }
    private boolean isInside(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }
    private boolean validateHelper(int row,int col,boolean[][] visited,int[] pathTilesVisited, int exitRow, int exitCol) {
    	visited[row][col] = true;
    	pathTilesVisited[0]++;
    	int neighborCount = 0;
    	int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    	for (int[] dir : directions) {
    	    int newRow = row + dir[0];
    	    int newCol = col + dir[1];

    	    if (isInside(newRow, newCol) && currentMap[newRow][newCol] == TileType.PATH) {
    	        neighborCount++;
    	    }
    	}
    	boolean isStartOrExit = (row == exitRow && col == exitCol) || (pathTilesVisited[0] == 1);

    	if (!isStartOrExit && neighborCount != 2) {
    	    return false;
    	}
    	if (isStartOrExit && neighborCount > 1) {
    	    return false;
    	}
    	for (int[] dir : directions) {
    	    int newRow = row + dir[0];
    	    int newCol = col + dir[1];

    	    if (isInside(newRow, newCol) && currentMap[newRow][newCol] == TileType.PATH && !visited[newRow][newCol]) {
    	        if (!validateHelper(newRow, newCol, visited, pathTilesVisited, exitRow, exitCol)) {
    	            return false;
    	        }
    	    }
    	}
    	return true;
    	
    }

    private boolean validateMap() {// checks the map if it has one path and no branches if it connected using recursion
    	boolean[][] visited = new boolean[ROWS][COLS];
    	int totalPathTiles = 0;
    	int startRow = -1;
    	int startCol = -1;
    	int exitRow = -1;
    	int exitCol = -1;
    	for (int row = 0; row < ROWS; row++) {
    	    for (int col = 0; col < COLS; col++) {
    	        if (currentMap[row][col] == TileType.PATH) {
    	            totalPathTiles++;
    	            if (isEdge(row, col)) {
    	                if (startRow == -1) {
    	                    startRow = row;
    	                    startCol = col;
    	                } else if (exitRow == -1) {
    	                    exitRow = row;
    	                    exitCol = col;
    	                } else {
    	                    return false;
    	                }
    	            }
    	        }
    	    }
    	}
    	if (startRow == -1 || exitRow == -1) {
    	    return false;
    	}
    	int[] pathTilesVisited = {0};
    	if (!validateHelper(startRow, startCol, visited, pathTilesVisited, exitRow, exitCol)) {
    	    
    	    return false;
    	}
    	if (pathTilesVisited[0] != totalPathTiles) {
    	   
    	    return false;
    	}
    	return true;
    	
    }
    @FXML
	private void handleBack() {
		Stage stage = (Stage)back.getScene().getWindow();
		stage.setScene(previousScene); 
		stage.setTitle("Main Menu");
        stage.show();
	}
    
    
    

}
