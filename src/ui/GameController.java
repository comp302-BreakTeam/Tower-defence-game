	package ui;
	
	import domain.*;
	
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	
	import domain.GameEngine;
	import domain.Maps;
	import domain.Player;
	import domain.TileType;
	import javafx.animation.AnimationTimer;
	import javafx.animation.TranslateTransition;
	import javafx.fxml.FXML;
	import javafx.geometry.Side;
	import javafx.scene.Scene;
	import javafx.scene.control.Button;
	import javafx.scene.control.ContextMenu;
	import javafx.scene.control.Label;
	import javafx.scene.control.MenuItem;
	import javafx.scene.control.ProgressBar;
	import javafx.scene.image.Image;
	import javafx.scene.image.ImageView;
	import javafx.scene.layout.GridPane;
	import javafx.stage.Stage;
	import javafx.util.Duration;
	
	public class GameController {
		public Player player;
		public Maps map;
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
	    @FXML
	    private Button pause;
	    @FXML
	    private Button toggle;
	    private GameEngine engine;
	    private List<int[]> path;
	    private AnimationTimer timer;
	    private Map<Enemy, ImageView> enemyViews = new HashMap<>();
	    private boolean isPaused=false;
	    private boolean gameSpeed=false;
	    
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
	
	
	
	
		public void setMap(Maps map) {
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
	            case ARCHER_TOWER:
	            	imagePath = "assets/Tower_archer128.png";
	            	break;
	            case MAGE_TOWER:
	            	imagePath = "assets/Tower_spell128.png";
	            	break;
	            case ARTILLERY_TOWER:
	            	imagePath = "assets/Tower_bomb128.png";
	            	break;
	            	
	            default:
	            	imagePath = "assets/grass.png";
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
	    			if(currentMap[row][col]==TileType.EMPTY_PLOT) {
	    				final int r = row;
	    				final int c = col;
		    			tile.setOnAction(e -> {
		    			ContextMenu menu = new ContextMenu();
	
		    			MenuItem archer = new MenuItem("Archer Tower");
		    			MenuItem mage = new MenuItem("Mage Tower");
		    			MenuItem artillery = new MenuItem("Artillery Tower");
		    		
		    			archer.setOnAction(ev -> {
		    		       setTileBackground(tile,TileType.ARCHER_TOWER);
		    		       map.setTile(r, c, new towerTile(TileType.ARCHER_TOWER));
   		    		       currentMap[r][c]=TileType.ARCHER_TOWER;
		    		       tile.setOnAction(null);
		    		    });
		    			mage.setOnAction(ev -> {
		    				setTileBackground(tile,TileType.MAGE_TOWER);
		    				map.setTile(r, c, new towerTile(TileType.MAGE_TOWER));
		    				currentMap[r][c]=TileType.MAGE_TOWER;
		    				tile.setOnAction(null);
		    		    });
		    			artillery.setOnAction(ev -> {
		    				setTileBackground(tile,TileType.ARTILLERY_TOWER);
		    				map.setTile(r, c, new towerTile(TileType.ARTILLERY_TOWER));
		    				currentMap[r][c]=TileType.ARTILLERY_TOWER;
		    				tile.setOnAction(null);
		    		    });
		    			
		    			menu.getItems().addAll(archer, mage, artillery);
		    			menu.show(tile, Side.RIGHT, 0, 0);
		    			
		    			});
	    			}
	    			mapGrid.add(tile, col, row);
				}
				
			}
			this.path=findPath();
			engine = new GameEngine(path, 10);
	        startGameLoop();
		}
		
		
		private void startGameLoop() {
			timer = new AnimationTimer() {
				private long lastUpdate = 0;
				 @Override
		            public void handle(long now) {
		                if (now - lastUpdate < 500_000_00) return;
		                lastUpdate = now;
		                
		                if(!isPaused) {	
		                engine.update();	
		                renderEnemies();			            		                
		                updateHUD();
		                
		                if(gameSpeed) {
		                	engine.update();	
			                renderEnemies();			            		                
			                updateHUD();
		                }
		                }
		               
	
		                if (engine.isGameOver()) {
		                    timer.stop();
		                    
		                }
		            }
		        };
		        timer.start();
			}
		
		private void renderEnemies() {
			for (Enemy e : engine.getActiveEnemies()) {
				ImageView view = enemyViews.get(e);
				
				if (view == null) {
					Image img;
					if(e.getClass()==Knight.class) {
						img = new Image(getClass().getResourceAsStream("assets/knight.png"));
					}
					else {
						img = new Image(getClass().getResourceAsStream("assets/goblin.png"));
					}
					
					view = new ImageView(img);
					view.setFitWidth(TILE_WIDTH * 0.7);
					view.setFitHeight(TILE_HEIGHT * 0.7);
					enemyViews.put(e, view);
					mapGrid.getChildren().add(view);
	                view.setTranslateX(e.getCol() * TILE_WIDTH);
	                view.setTranslateY(e.getRow() * TILE_HEIGHT);
					
				}
				else {
					TranslateTransition tt = new TranslateTransition(Duration.millis(300), view);
					double targetx = e.getCol() * TILE_WIDTH ;
	                double targety = e.getRow() * TILE_HEIGHT ;
	                double movx = targetx - view.getTranslateX();
	                double movy = targety - view.getTranslateY();
	                if (movy>0) {
						movy-=30;
					}
	                else if(movy<0) {
						movy-=10;
					}
	                tt.setByX(movx);
	                tt.setByY(movy);
	                tt.play();
				}
			}
			
			for (Enemy e : enemyViews.keySet()) {
				if (!engine.getActiveEnemies().contains(e)) {
					mapGrid.getChildren().remove(enemyViews.get(e));
					
				}
			}
		}
		
		private void updateHUD() {
	        numHealth.setText("" + player.getLives());
	        numCoin.setText("" + player.getGold());
	        numWave.setText("" + engine.getWaveNumber());
	    }
		@FXML
		private void handleBack() {
			Stage stage = (Stage)back.getScene().getWindow();
			stage.setScene(previousScene); 
			stage.setTitle("Main Menu");
	        stage.show();
		}
		private boolean isEdge(int row, int col) {
	        return row == 0 || row == ROWS - 1 || col == 0 || col == COLS - 1;
	    }
		private boolean isInside(int row, int col) {
	        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
	    }
		
		private List<int[]> findPath(){
			boolean[][] visited = new boolean[ROWS][COLS];
		    List<int[]> path = new ArrayList<>();
			int startRow = -1;
	    	int startCol = -1;
	    	 for (int row = 0; row < ROWS; row++) {
	    	        for (int col = 0; col < COLS; col++) {
	    	            if (currentMap[row][col] == TileType.PATH && isEdge(row, col)) {
	    	                startRow = row;
	    	                startCol = col;
	    	                break;
	    	            }
	    	        }
	    	        if (startRow != -1) break;
	    	    }
	    	 dfsPath(startRow, startCol, visited, path);
			return path;
		}
		private boolean dfsPath(int row, int col, boolean[][] visited, List<int[]> path) {
			if (!isInside(row, col) || visited[row][col] || currentMap[row][col] != TileType.PATH) {
		        return false;
		    }
			visited[row][col] = true;
		    path.add(new int[]{row, col});
		    if (isEdge(row, col) && path.size() > 1) {
		        
		        return true;
		    }
		    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
		    for (int[] dir : directions) {
		        int newRow = row + dir[0];
		        int newCol = col + dir[1];
		        if (dfsPath(newRow, newCol, visited, path)) {
		            return true;
		        }
		    }
		    return false;
		}
		@FXML
		private void handlePause(){
			isPaused=!isPaused;	
			}
	
		@FXML
		private void toggleSpeed() {
			
			gameSpeed=!gameSpeed;
			if(gameSpeed) {
				toggle.setText("FAST");
			}
			else {
				toggle.setText("NORMAL");
			}
		}
		}
