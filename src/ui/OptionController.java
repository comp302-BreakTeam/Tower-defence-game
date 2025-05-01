package ui;

import domain.Player;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class OptionController {
	public Player player;
	private Scene previousScene;

	public void setPlayer(Player player) {
		this.player= player;
		
		
	}
	public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
@FXML private Slider volumeSlider;
@FXML private ComboBox<String> graphicsComboBox;
@FXML private Button backButton;

@FXML
public void initialize() {
    graphicsComboBox.setValue("Medium"); // default
    backButton.setOnAction(e -> {
        if (previousScene != null) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(previousScene);
        }
    });
}

}
