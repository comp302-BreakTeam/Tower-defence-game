// MapSelectorController.java
package ui;

import java.io.File;
import java.io.IOException;

import domain.Maps;
import domain.Player;
import domain.SaveMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
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
	private FlowPane mapGrid;

	private String selectedMapName;

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
			Alert alert = new Alert(AlertType.ERROR, "No maps found.");
			alert.showAndWait();
			Stage stage = (Stage) back.getScene().getWindow();
			stage.setScene(previousScene);
			stage.setTitle("Main Menu");
			stage.show();
			return;
		}

		File[] files = mapsFolder.listFiles((dir, name) -> name.endsWith(".dat"));
		if (files != null) {
			for (File file : files) {
				String name = file.getName().replace(".dat", "");

				Image preview = new Image("file:maps/" + name + ".png", 200, 150, true, true);
				ImageView previewView = new ImageView(preview);

				Label label = new Label(name);
				VBox mapBox = new VBox(previewView, label);
				mapBox.setPrefSize(150, 150);
				mapBox.setStyle(
						"-fx-border-color: green;" +
								"-fx-border-radius: 30;" +
								"-fx-background-color: #f9f9f9;" +
								"-fx-padding: 10;" +
								"-fx-alignment: center;" +
								"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 5, 0.5, 1, 1);"
				);
				mapBox.setSpacing(15);
				mapBox.setCursor(Cursor.HAND);
				mapBox.setOnMouseClicked((MouseEvent e) -> {
					selectedMapName = name;
					highlightSelected(mapBox);
				});

				mapGrid.getChildren().add(mapBox);
			}
		}
	}

	private void highlightSelected(VBox selected) {
		for (javafx.scene.Node node : mapGrid.getChildren()) {
			node.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 5;");
		}
		selected.setStyle("-fx-border-color: blue; -fx-border-width: 3; -fx-padding: 5;");
	}

	public void setPlayer(Player player) {
		this.player = player;
		playerName.setText(player.getName());
	}

	public void setPreviousScene(Scene scene) {
		this.previousScene = scene;
	}

	@FXML
	private void handleBack() {
		Stage stage = (Stage) back.getScene().getWindow();
		stage.setScene(previousScene);
		stage.setTitle("Main Menu");
		stage.show();
	}

	@FXML
	private void loadSelectedMap() throws ClassNotFoundException, IOException {
		if (selectedMapName == null) {
			Alert alert = new Alert(AlertType.ERROR, "No map selected.");
			alert.showAndWait();
			return;
		}

		Maps loadedMap = SaveMap.loadMap(selectedMapName + ".dat");

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
		Parent root = loader.load();
		GameController controller = loader.getController();
		Scene currentScene = play.getScene();
		controller.setPreviousScene(currentScene);
		controller.setPlayer(player);
		controller.setMap(loadedMap);
		controller.generateMap();

		Stage stage = (Stage) play.getScene().getWindow();
		stage.setScene(new Scene(root, 1200, 800));
		stage.setTitle("Game");
		stage.show();
	}
}
