package ui;

import domain.Player;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class OptionController {
	public Player player;
	private Scene previousScene;
	private static double savedVolume = 50;
	private static int savedLives = 10;
	private static int savedGold = 500;
	private static int savedWaveSize = 10;
	private static int savedMaxWave = 5;
	private double originalVolume;
	private int originalLives;
	private int originalGold;
	private int originalMaxWave;
	private int originalWaveSize;

	public void setPlayer(Player player) {
		this.player= player;
		
		
	}
	public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
@FXML private Slider volumeSlider;
@FXML private Spinner<Integer> livesSpinner;
@FXML private Spinner<Integer> goldSpinner;
@FXML private Spinner<Integer> maxWaveSpinner;
@FXML private Spinner<Integer> waveSizeSpinner;
@FXML private Button saveButton;
@FXML private Button discardButton;

@FXML
public void initialize() {
    
    livesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, savedLives));
    goldSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, savedGold));
    maxWaveSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, savedMaxWave));
    waveSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, savedWaveSize));
    
    livesSpinner.setEditable(true);
    goldSpinner.setEditable(true);
    maxWaveSpinner.setEditable(true);
    waveSizeSpinner.setEditable(true);
    
	
    volumeSlider.setValue(savedVolume);
    
    originalLives = savedLives;
    originalGold = savedGold;
    originalMaxWave = savedMaxWave;
    originalWaveSize = savedWaveSize;

    
    javafx.application.Platform.runLater(() -> {
        originalVolume = volumeSlider.getValue();
    });

    saveButton.setOnAction(e -> {
    	savedVolume = volumeSlider.getValue();
        savedLives = livesSpinner.getValue();
        savedGold = goldSpinner.getValue();
        savedMaxWave = maxWaveSpinner.getValue();
        savedWaveSize = waveSizeSpinner.getValue();
        
        if (player != null) {
            player.setLives(savedLives);
            player.setGold(savedGold);
            player.setMaxWave(savedMaxWave);
            player.setWaveSize(savedWaveSize);
        }

        if (previousScene != null) {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setScene(previousScene);
        }
    });

    discardButton.setOnAction(e -> {
    	volumeSlider.setValue(originalVolume);
        livesSpinner.getValueFactory().setValue(originalLives);
        goldSpinner.getValueFactory().setValue(originalGold);
        maxWaveSpinner.getValueFactory().setValue(originalMaxWave);
        waveSizeSpinner.getValueFactory().setValue(originalWaveSize);

        if (previousScene != null) {
            Stage stage = (Stage) discardButton.getScene().getWindow();
            stage.setScene(previousScene);
        }
    });
}

}
