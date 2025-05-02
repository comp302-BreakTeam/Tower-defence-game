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
	private static int savedLives = 20;
	private static double savedSpeed = 1.0;
	private static int savedGold = 500;
	private static int savedWave = 1;
	private double originalVolume;
	private int originalLives;
	private double originalSpeed;
	private int originalGold;
	private int originalWave;

	public void setPlayer(Player player) {
		this.player= player;
		
		
	}
	public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
@FXML private Slider volumeSlider;
@FXML private Spinner<Integer> livesSpinner;
@FXML private Spinner<Double> enemySpeedSpinner;
@FXML private Spinner<Integer> goldSpinner;
@FXML private Spinner<Integer> waveSpinner;
@FXML private Button saveButton;
@FXML private Button discardButton;

@FXML
public void initialize() {
    
    livesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, savedLives));
    goldSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, savedGold));
    waveSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, savedWave));
    enemySpeedSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.5, 5.0, savedSpeed, 0.5));
	
    volumeSlider.setValue(savedVolume);
    
    originalSpeed = savedSpeed;
    originalLives = savedLives;
    originalGold = savedGold;
    originalWave = savedWave;

    
    javafx.application.Platform.runLater(() -> {
        originalVolume = volumeSlider.getValue();
    });

    saveButton.setOnAction(e -> {
    	savedVolume = volumeSlider.getValue();
        savedLives = livesSpinner.getValue();
        savedSpeed = enemySpeedSpinner.getValue();
        savedGold = goldSpinner.getValue();
        savedWave = waveSpinner.getValue();

        if (previousScene != null) {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setScene(previousScene);
        }
    });

    discardButton.setOnAction(e -> {
    	volumeSlider.setValue(originalVolume);
        livesSpinner.getValueFactory().setValue(originalLives);
        enemySpeedSpinner.getValueFactory().setValue(originalSpeed);
        goldSpinner.getValueFactory().setValue(originalGold);
        waveSpinner.getValueFactory().setValue(originalWave);

        if (previousScene != null) {
            Stage stage = (Stage) discardButton.getScene().getWindow();
            stage.setScene(previousScene);
        }
    });
}

}
