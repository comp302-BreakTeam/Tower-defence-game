package ui;


import domain.Map;
import domain.Player;
import domain.TileType;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameController {
	public Player player;
	public Map map;
	@FXML
	private GridPane mapGrid;
	private Scene previousScene;
	private static final int ROWS = 16;
    private static final int COLS = 16;
    private static final int TILE_WIDTH = 75;
    private static final int TILE_HEIGHT = 50;
    private TileType[][] currentMap = new TileType[ROWS][COLS];
    @FXML
	private Label playerName;
    @FXML
	private Label numHealth;
    @FXML
	private Label numCoin;
    @FXML
	private Label numWave;
    @FXML
    private ImageView health;
    @FXML
    private ImageView coin;
    @FXML
    private ImageView wave;
    @FXML
	private Button back;
    
    
    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
    
    
    
    
    public void setPlayer(Player player) {
		this.player = player;
		playerName.setText(player.getName());
		numHealth.setText(""+player.getLives()+"");
		numCoin.setText(""+player.getGold()+"");
		numWave.setText(""+player.getWave()+"");
		Image healthImage = new Image(getClass().getResourceAsStream("assets/health.png"));
		health.setImage(healthImage);
		Image coinImage = new Image(getClass().getResourceAsStream("assets/gold.png"));
		coin.setImage(coinImage);
		Image waveImage = new Image(getClass().getResourceAsStream("assets/wave.png"));
		wave.setImage(waveImage);
		
		
	}




	public void setMap(Map map) {
		this.map = map;
	}




	public void setTileBackground(Button tile, TileType type) {
        String imagePath = "assets/grass.png"; 

        switch (type) {
            case PATH:
                imagePath = "assets/path.png";
                break;
            case GRASS:
                imagePath = "assets/grass.png";
                break;
            case ROCK:
                imagePath = "assets/rock.png";
                break;
            case TREE:
                imagePath = "assets/tree.png";
                break;
            case EMPTY_PLOT:
                imagePath = "assets/empty_plot.png";
                break;
            
        }

        tile.setStyle(
            "-fx-background-image: url('" + getClass().getResource(imagePath) + "');" +
            "-fx-background-size: cover;"
        );
    }
	
	public void generateMap() {
		
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				currentMap[row][col] = map.getTile(row, col).getType();
				Button tile = new Button();
    			tile.setPrefSize(TILE_WIDTH, TILE_HEIGHT);
    			setTileBackground(tile, currentMap[row][col] );
				if(map.getTile(row, col).getType()==TileType.EMPTY_PLOT){
					tile.setOnAction(e->{
						
					});
				}
    			mapGrid.add(tile, col, row);
			}
			
		}
	}
	@FXML
	private void handleBack() {
		Stage stage = (Stage)back.getScene().getWindow();
		stage.setScene(previousScene); 
		stage.setTitle("Main Menu");
        stage.show();
	}
	

}
