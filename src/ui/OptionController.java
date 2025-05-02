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
	private static double savedVolume = 50;
	private static String savedGraphics = "Medium";
	private double originalVolume;
	private String originalGraphicsQuality;

	public void setPlayer(Player player) {
		this.player= player;
		
		
	}
	public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
@FXML private Slider volumeSlider;
@FXML private ComboBox<String> graphicsComboBox;
@FXML private Button saveButton;
@FXML private Button discardButton;

@FXML
public void initialize() {
    graphicsComboBox.getItems().addAll("Low", "Medium", "High");
    graphicsComboBox.setValue("Medium");
    
    volumeSlider.setValue(savedVolume);
    graphicsComboBox.setValue(savedGraphics);
    
    originalVolume = savedVolume;
    originalGraphicsQuality = savedGraphics;

    
    javafx.application.Platform.runLater(() -> {
        originalVolume = volumeSlider.getValue();
        originalGraphicsQuality = graphicsComboBox.getValue();
    });

    originalVolume = volumeSlider.getValue();
    originalGraphicsQuality = graphicsComboBox.getValue();

    saveButton.setOnAction(e -> {
        savedVolume = volumeSlider.getValue();
        savedGraphics = graphicsComboBox.getValue();

        if (previousScene != null) {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setScene(previousScene);
        }
    });

    discardButton.setOnAction(e -> {
        volumeSlider.setValue(originalVolume);
        graphicsComboBox.setValue(originalGraphicsQuality);

        if (previousScene != null) {
            Stage stage = (Stage) discardButton.getScene().getWindow();
            stage.setScene(previousScene);
        }
    });
}

}
