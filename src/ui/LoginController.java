package ui;

import java.io.IOException;

import domain.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class LoginController {

    @FXML
    private TextField name;

    @FXML
    private Button button;
    @FXML
    private void playerLogin() throws IOException {
    	String playerName = name.getText().trim();
    	if (!playerName.isEmpty()) {
    		Player player = new Player(playerName, 10, 500);
    		System.out.println("Player created: " + player.getName());
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
    		Parent root = loader.load();
    		MainMenuController controller= loader.getController();
    		controller.setPlayer(player);
    		Stage stage = (Stage)button.getScene().getWindow();
    		stage.setScene(new Scene(root, 1200, 800)); 
            stage.show();
		}
    	else {
    		System.out.println("Name can't be empty");
    		Alert alert= new Alert(AlertType.ERROR,"Name can't be empty");
    		alert.showAndWait();
		}
    }
    

    
}
