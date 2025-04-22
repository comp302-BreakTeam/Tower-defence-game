package ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MapController {
	private Scene previousScene;
	private static final int ROWS = 16;
    private static final int COLS = 16;
    private static final int TILE_WIDTH = 75;
    private static final int TILE_HEIGHT = 50;
    
    @FXML
    private ComboBox<String> tileSelector;
    
    @FXML
    private GridPane mapGrid;
    @FXML
    private Button saveButton;
    @FXML
	private void handleSave() throws IOException {
		
		Stage stage = (Stage)saveButton.getScene().getWindow();
		stage.setScene(previousScene); 
		stage.setTitle("Main Menu");
        stage.show();
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
    			tile.setStyle(
    			    "-fx-background-image: url('" + getClass().getResource("assets/grass.png") + "');" +
    			    "-fx-background-size: cover;" 
    			);
    			tile.setOnAction(e->{
    				TileType selected = TileType.valueOf(tileSelector.getValue());
    				switch (selected) {
    			    case PATH:
    			    	tile.setStyle(
    		    			    "-fx-background-image: url('" + getClass().getResource("assets/path.png") + "');" +
    		    			    "-fx-background-size: cover;" 
    		    			);
    			        break;
    			    case GRASS:
    			    	tile.setStyle(
    		    			    "-fx-background-image: url('" + getClass().getResource("assets/grass.png") + "');" +
    		    			    "-fx-background-size: cover;" 
    		    			);
    			        break;
    			    case EMPTY_PLOT:
    			    	tile.setStyle(
    		    			    "-fx-background-image: url('" + getClass().getResource("assets/empty_plot.png") + "');" +
    		    			    "-fx-background-size: cover;" 
    		    			);
    			        break;
    			    case ROCK:
    			    	tile.setStyle(
    		    			    "-fx-background-image: url('" + getClass().getResource("assets/rock.png") + "');" +
    		    			    "-fx-background-size: cover;" 
    		    			);
    			        break;
    			    case TREE:
    			    	tile.setStyle(
    		    			    "-fx-background-image: url('" + getClass().getResource("assets/tree.png") + "');" +
    		    			    "-fx-background-size: cover;" 
    		    			);
    			        break;
    			}
    			});
    			mapGrid.add(tile, col, row);
    		}
    	}
    }
    
    

}
