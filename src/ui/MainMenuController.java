package ui;



import domain.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainMenuController {
	public Player player;
	@FXML
    private Button quit;
	@FXML
    private Button mapButton;
	@FXML
	private Label playerName;
	@FXML
	private void handleQuit() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Quit");
		alert.setHeaderText("Are You Sure You Want to Quit");
		if (alert.showAndWait().get()==ButtonType.OK) {
			Stage stage = (Stage) quit.getScene().getWindow();
			stage.close();
		}
		
		
	}
	@FXML
	private void handleMapEditor() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("MapEditor.fxml"));
		Stage stage = (Stage)mapButton.getScene().getWindow();
		stage.setScene(new Scene(root, 1200, 800)); 
        stage.show();
	}
	public void setPlayer(Player player) {
		this.player= player;
		playerName.setText(player.getName());
		
	}

}
