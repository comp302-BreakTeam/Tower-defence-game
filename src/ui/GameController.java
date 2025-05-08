	package ui;
	
	import domain.*;
	
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
    import javafx.animation.AnimationTimer;
	import javafx.animation.KeyFrame;
	import javafx.animation.Timeline;
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
		private Map<Enemy, Timeline> enemyAnimations = new HashMap<>();
		private Map<Enemy, Image[]> enemyImages = new HashMap<>();
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
	    private ImageView gameOver;
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
	    private Map<Enemy, ProgressBar> enemyHP = new HashMap<>();
	    private int defaultHealth;
	    private int defaultCoins;
	    
	    public void setPreviousScene(Scene scene) {
	        this.previousScene = scene;
	    }
	    
	    
	    
	    
	    public void setPlayer(Player player) {
			this.player = player;
			playerName.setText(player.getName());
			numHealth.setText(""+player.getLives()+"");
			numCoin.setText(""+player.getGold()+"");
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
	            	imagePath = "assets/Archer_Tower.png";
	            	break;
	            case MAGE_TOWER:
	            	imagePath = "assets/Mage_Tower.png";
	            	break;
	            case ARTILLERY_TOWER:
	            	imagePath = "assets/Artillery_Tower.png";
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
		    			emptyPlotAction(r, c, tile); 
		    			
		    			});
	    			}
	    			mapGrid.add(tile, col, row);
				}
				
			}
			this.path=findPath();
			defaultCoins=player.getGold();
			defaultHealth=player.getLives();
			engine = new GameEngine(path, player.getWaveSize(), player.getMaxWave(),player);
			updateHUD();
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
		                    gameOver.setVisible(true);
		                    mapGrid.getChildren().remove(gameOver);
		                    mapGrid.getChildren().add(gameOver);


		                }
		            }
		        };
		        timer.start();
			}

		private void renderEnemies() {
			for (Enemy e : engine.getActiveEnemies()) {
				ImageView view = enemyViews.get(e);
				ProgressBar HP = enemyHP.get(e);

				if (view == null) {
					Image[] frames;
					if (e instanceof Knight) {
						frames = new Image[]{
								new Image(getClass().getResourceAsStream("/ui/Assets/knight_run_1.png")),
								new Image(getClass().getResourceAsStream("/ui/Assets/knight_run_2.png")),
								new Image(getClass().getResourceAsStream("/ui/Assets/knight_run_3.png")),
								new Image(getClass().getResourceAsStream("/ui/Assets/knight_run_4.png")),
								new Image(getClass().getResourceAsStream("/ui/Assets/knight_run_5.png")),
								new Image(getClass().getResourceAsStream("/ui/Assets/knight_run_6.png"))
						};
					} else {
						frames = new Image[]{
								new Image(getClass().getResourceAsStream("/ui/Assets/goblin_run_1.png")),
								new Image(getClass().getResourceAsStream("/ui/Assets/goblin_run_2.png")),
								new Image(getClass().getResourceAsStream("/ui/Assets/goblin_run_3.png")),
								new Image(getClass().getResourceAsStream("/ui/Assets/goblin_run_4.png")),
								new Image(getClass().getResourceAsStream("/ui/Assets/goblin_run_5.png")),
								new Image(getClass().getResourceAsStream("/ui/Assets/goblin_run_6.png"))
						};
					}

					view = new ImageView(frames[0]);
					view.setFitWidth(TILE_WIDTH * 1.3);
					view.setFitHeight(TILE_HEIGHT * 1.3);
					enemyViews.put(e, view);

					HP = new ProgressBar(1);
					HP.setPrefHeight(10);
					HP.setPrefWidth(TILE_WIDTH * 0.7);
					HP.setStyle("-fx-accent: red;");
					enemyHP.put(e, HP);

					mapGrid.getChildren().addAll(view, HP);
					view.setTranslateX(e.getCol() * TILE_WIDTH);
					view.setTranslateY(e.getRow() * TILE_HEIGHT);
					HP.setTranslateX(view.getTranslateX());
					HP.setTranslateY(view.getTranslateY() - 30);

					final ImageView finalView = view;
					final Image[] finalFrames = frames;

					Timeline animation = new Timeline(new KeyFrame(Duration.millis(100), evt -> {
						int frameIndex = (int) ((System.currentTimeMillis() / 100) % finalFrames.length);
						finalView.setImage(finalFrames[frameIndex]);
					}));
					animation.setCycleCount(Timeline.INDEFINITE);
					animation.play();
				} else {
					TranslateTransition tt = new TranslateTransition(Duration.millis(250), view);
					TranslateTransition hh = new TranslateTransition(Duration.millis(250), HP);
					double targetx = e.getCol() * TILE_WIDTH;
					double targety = e.getRow() * TILE_HEIGHT;
					double movx = targetx - view.getTranslateX();
					double movy = targety - view.getTranslateY();
					if (movy > 0) {
						movy -= 30;
					} else if (movy < 0) {
						movy -= 10;
					}
					tt.setByX(movx);
					hh.setByX(movx);
					tt.setByY(movy);
					hh.setByY(movy);
					tt.play();
					hh.play();
					HP.setProgress(e.getHealth() / e.maxHP);
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
			isPaused=true;
			player.setGold(defaultCoins);
			player.setLives(defaultHealth);
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
		private void towerTileAction(int r,int c,Button tile) {
			ContextMenu menu = new ContextMenu();
			MenuItem upgrade = new MenuItem("Upgrade");
			MenuItem sell = new MenuItem("Sell");
			upgrade.setOnAction(e ->{
				towerTile twrTile= (towerTile) map.getTile(r, c);
				Tower tower= twrTile.getTower();
				if(player.canAfford(tower.getCost())) {
					tower.upgradeTower();
				}
				
				
			});
			sell.setOnAction(e->{
				towerTile twrTile= (towerTile) map.getTile(r, c);
				Tower tower= twrTile.getTower();
				player.addGold(tower.sellTower());
				map.setTile(r, c, new fixEmptyTile(TileType.EMPTY_PLOT));
				setTileBackground(tile, TileType.EMPTY_PLOT);
				tile.setOnAction(ev -> emptyPlotAction(r, c, tile)); 
			});
			 menu.getItems().addAll(upgrade, sell);
			 menu.show(tile, Side.RIGHT, 0, 0);
		}
		private void emptyPlotAction(int r,int c,Button tile){
			ContextMenu menu = new ContextMenu();
			
			MenuItem archer = new MenuItem("Archer Tower");
			MenuItem mage = new MenuItem("Mage Tower");
			MenuItem artillery = new MenuItem("Artillery Tower");
		
			archer.setOnAction(ev -> {
				Archer_Tower tower = new Archer_Tower();
				if(player.canAfford(tower.getCost())) {
					setTileBackground(tile,TileType.ARCHER_TOWER);
	    		       map.setTile(r, c, new towerTile(TileType.ARCHER_TOWER));
	    		       towerTile twrTile= (towerTile) map.getTile(r, c);
	    		       twrTile.setTower(tower);
		    		       currentMap[r][c]=TileType.ARCHER_TOWER;
		    		       tile.setOnAction(e -> towerTileAction(r, c, tile));
				}
		       
		    });
			mage.setOnAction(ev -> {
				Mage_Tower tower = new Mage_Tower();
				if(player.canAfford(tower.getCost())) {
					setTileBackground(tile,TileType.MAGE_TOWER);
    				map.setTile(r, c, new towerTile(TileType.MAGE_TOWER));
    				towerTile twrTile= (towerTile) map.getTile(r, c);
    				twrTile.setTower(tower);
    				currentMap[r][c]=TileType.MAGE_TOWER;
    				tile.setOnAction(e -> towerTileAction(r, c, tile));
				}
				
		    });
			artillery.setOnAction(ev -> {
				Artillery_Tower tower = new Artillery_Tower();
				if(player.canAfford(tower.getCost())) {
					setTileBackground(tile,TileType.ARTILLERY_TOWER);
    				map.setTile(r, c, new towerTile(TileType.ARTILLERY_TOWER));
    				towerTile twrTile= (towerTile) map.getTile(r, c);
    				twrTile.setTower(tower);
    				currentMap[r][c]=TileType.ARTILLERY_TOWER;
    				tile.setOnAction(e -> towerTileAction(r, c, tile));
				}
				
		    });
			
			menu.getItems().addAll(archer, mage, artillery);
			menu.show(tile, Side.RIGHT, 0, 0);
			
		}
		}
