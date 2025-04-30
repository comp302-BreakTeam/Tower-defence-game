package ui;

import domain.Player;
import javafx.scene.Scene;

public class OptionController {
	public Player player;
	private Scene previousScene;

	public void setPlayer(Player player) {
		this.player= player;
		
		
	}
	public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

}
