package ui;



import java.io.File;
import java.io.IOException;

import domain.Map;
import domain.Player;
import domain.SaveMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MapSelectorController {
	public Player player;
	private Scene previousScene;
	@FXML
	private Button back;
	@FXML
	private Button play;
	@FXML
	private Label playerName;
	@FXML
    private ListView<String> mapList;
	
	@FXML
    private void initialize() {
		play.setStyle(
	            "-fx-background-image: url('" + getClass().getResource("assets/play.png") + "');" +
	            "-fx-background-size: cover;"
	     );
		loadAvailableMaps();
	}
	private void loadAvailableMaps() {
        File mapsFolder = new File("maps");
        if (!mapsFolder.exists()) {
        	Alert alert= new Alert(AlertType.ERROR,"No maps is found.");
    		alert.showAndWait();
    		Stage stage = (Stage)back.getScene().getWindow();
    		stage.setScene(previousScene); 
    		stage.setTitle("Main Menu");
            stage.show();
            return;
        }

        File[] files = mapsFolder.listFiles((dir, name) -> name.endsWith(".dat"));
        if (files != null) {
            for (File file : files) {
            	String name = file.getName();
            	name = name.substring(0, name.length() - 4);
            	mapList.getItems().add(name);
            }
        }
    }
	
	
	
	public void setPlayer(Player player) {
		this.player= player;
		playerName.setText(player.getName());
		
		
	}
	public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
	@FXML
	private void handleBack() {
		Stage stage = (Stage)back.getScene().getWindow();
		stage.setScene(previousScene); 
		stage.setTitle("Main Menu");
        stage.show();
	}
	@FXML
	private void loadSelectedMap() throws ClassNotFoundException, IOException {
		String selectedMap = mapList.getSelectionModel().getSelectedItem();
		if (selectedMap == null) {
			Alert alert= new Alert(AlertType.ERROR,"No maps selected.");
    		alert.showAndWait();
		}
		Map loadedMap = SaveMap.loadMap(selectedMap + ".dat");
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
        Parent root = loader.load();
        GameController controller = loader.getController();
        Scene currentScene = play.getScene();
        controller.setPreviousScene(currentScene);
        controller.setPlayer(player);
        controller.setMap(loadedMap);
        controller.generateMap();
        Stage stage = (Stage)play.getScene().getWindow();
		stage.setScene(new Scene(root, 1200, 800)); 
		stage.setTitle("Game");
        stage.show();
	}

}
