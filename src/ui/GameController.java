	package ui;
	
	import domain.*;

	import java.util.*;

	import javafx.animation.*;
	import javafx.fxml.FXML;
	import javafx.geometry.Side;
	import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
		private List<ProjectileController> activeProjectiles = new ArrayList<>();
		private Map<Tower, Long> towerLastFireTimes = new HashMap<>();
		private static final long FIRE_COOLDOWN_NS = 800_000_000; // 800ms in nanoseconds
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
	    @FXML
	    private Button fireball;
	    
	    private GameEngine engine;
	    private List<int[]> path;
	    private AnimationTimer timer;
	    private Map<Enemy, ImageView> enemyViews = new HashMap<>();
	    private boolean isPaused=false;
	    private boolean gameSpeed=false;
	    private Map<Enemy, ProgressBar> enemyHP = new HashMap<>();
	    private Map<Enemy, ImageView> enemyThunder = new HashMap<>();
	    private Map<Enemy, ImageView> enemySlow = new HashMap<>();
	    private int defaultHealth;
	    private int defaultCoins;
	    private int startrow;
	    private int startcol;
	    private double speedMultiplier = 1.0;
	    private boolean fireballMode = false;
	    private boolean fireballOnCooldown = false;
	    private Map<GoldBag, ImageView> goldBagViews = new HashMap<>();
		private Map<Wood, ImageView> woodViews = new HashMap<>();
		private List<Wood> woods = new ArrayList<>();
		private ScaleTransition fireballPulse;
		private Label fireballCooldownLabel = new Label();
	    
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
	
	
	
	
		public void setTileBackground(Button tile, TileType type) {//for setting background
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
					final int r = row;
    				final int c = col;
					tile.setOnMouseClicked(e -> {// gives each tile action so it can drop fire ball on every tile
					    if (fireballMode) {
					        
					        dropFireballAt(r, c);
					        fireballMode = false;
					        
					    }
					});
	    			tile.setPrefSize(TILE_WIDTH, TILE_HEIGHT);
	    			setTileBackground(tile, currentMap[row][col] );
	    			if(currentMap[row][col]==TileType.EMPTY_PLOT) {
	    				
		    			tile.setOnAction(e -> { //action to put tower on empty plots
		    			emptyPlotAction(r, c, tile); 
		    			
		    			});
	    			}
	    			mapGrid.add(tile, col, row);
				}
				
			}
			this.path=findPath(); //sets the path using findpath
			defaultCoins=player.getGold();
			defaultHealth=player.getLives();
			engine = new GameEngine(path, player.getWaveSize(), player.getMaxWave(),player);// initializes a game engine 
			updateHUD();
			spawnRandomWoods();
			renderWoods();
	        startGameLoop(); // starts the game loop
			fireballCooldownLabel.setStyle(
					"-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold; " +
							"-fx-effect: dropshadow(gaussian, black, 4, 0.7, 0, 0);"
			);
			fireballCooldownLabel.setVisible(false);
			fireballCooldownLabel.setMouseTransparent(true); // So it doesn't block clicks
			startFireballPulse();
		}


		private void dropFireballAt(int row, int col) {
			Image fireballImage = new Image(getClass().getResourceAsStream("/ui/Assets/fireball_1.png"));
			ImageView fallingFireball = new ImageView(fireballImage); // Renamed variable
			fallingFireball.setFitWidth(100);
			fallingFireball.setFitHeight(100);

			double targetX = col * TILE_WIDTH;
			double targetY = row * TILE_HEIGHT;
			fallingFireball.setTranslateX(targetX);
			fallingFireball.setTranslateY(-100);
			mapGrid.getChildren().add(fallingFireball);
			TranslateTransition fall = new TranslateTransition(Duration.seconds(0.6), fallingFireball); // animation so the fireball drops
			fall.setToY(targetY);
			fall.setOnFinished(e -> { // things to do after drop
				mapGrid.getChildren().remove(fallingFireball);
				playExplosion(targetX, targetY);
			});
			fall.play();

			fireballOnCooldown = true;
			stopFireballPulse();
			stopFireballPulse();

			fireball.setVisible(true);
			fireball.setText("10");

			Timeline cooldownTimer = new Timeline();
			final int[] secondsLeft = {10};
			KeyFrame kf = new KeyFrame(Duration.seconds(1), ev -> {
				secondsLeft[0]--;
				if (secondsLeft[0] > 0) {
					fireball.setText(String.valueOf(secondsLeft[0]));
				} else {
					fireballOnCooldown = false;
					fireball.setText("FIREBALL ACTIVE");
					startFireballPulse();
					cooldownTimer.stop();
				}
			});
			cooldownTimer.getKeyFrames().add(kf);
			cooldownTimer.setCycleCount(10);
			fireball.setText("COOLDOWN");
			cooldownTimer.play();
		}
		private void playExplosion(double x, double y) {
		    ImageView explosion = new ImageView();
		    explosion.setFitWidth(200);
		    explosion.setFitHeight(200);
		    explosion.setTranslateX(x - 25);
		    explosion.setTranslateY(y - 25);

		    Image[] explosionFrames = new Image[] {
		        new Image(getClass().getResourceAsStream("/ui/Assets/explosion_1.png")),
		        new Image(getClass().getResourceAsStream("/ui/Assets/explosion_2.png")),
		        new Image(getClass().getResourceAsStream("/ui/Assets/explosion_3.png")),
		        new Image(getClass().getResourceAsStream("/ui/Assets/explosion_4.png")),
		        new Image(getClass().getResourceAsStream("/ui/Assets/explosion_5.png")),
		        new Image(getClass().getResourceAsStream("/ui/Assets/explosion_6.png")),
		        new Image(getClass().getResourceAsStream("/ui/Assets/explosion_7.png")),
		        new Image(getClass().getResourceAsStream("/ui/Assets/explosion_8.png")),
		        new Image(getClass().getResourceAsStream("/ui/Assets/explosion_9.png"))
		    };

		    mapGrid.getChildren().add(explosion);

		    Timeline animation = new Timeline();
		    for (int i = 0; i < explosionFrames.length; i++) {
		        final int frame = i;
		        animation.getKeyFrames().add(new KeyFrame(Duration.millis(i * 100), e -> {
		            explosion.setImage(explosionFrames[frame]);
		        }));
		    }
		    animation.setOnFinished(e -> mapGrid.getChildren().remove(explosion));
		    animation.play();
		    double aoeRadius = 100;// pixel radius for damage enemies
		    for (Enemy enemy : engine.getActiveEnemies()) {
		        double dx = enemy.getxCoordinate() - x;
		        double dy = enemy.getyCoordinate() - y;
		        double dist = Math.sqrt(dx * dx + dy * dy);
		        if (dist <= aoeRadius) {
		            enemy.takeDamage(100); //insta death for every enemy
		        }
		    }
		}


		
		private long lastUpdateTime = System.nanoTime();
		
		private void startGameLoop() {
			timer = new AnimationTimer() {

				private long lastUpdate = 0;
				@Override
				public void handle(long now) {
					if (now - lastUpdate < 500_000_00) return;
					lastUpdate = now;

					double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
					lastUpdateTime = now;

					if(!isPaused) {
						engine.update();
						// TEMP: Find first tower and first enemy, fire once for testing
						long now2 = System.nanoTime(); // at top of handle()

						if (!engine.getActiveEnemies().isEmpty()) {

							for (int r = 0; r < ROWS; r++) {
								for (int c = 0; c < COLS; c++) {
									if (currentMap[r][c] == TileType.ARCHER_TOWER ||
											currentMap[r][c] == TileType.MAGE_TOWER ||
											currentMap[r][c] == TileType.ARTILLERY_TOWER) {

										Tile tileObj = map.getTile(r, c);
										if (tileObj instanceof towerTile) {
											towerTile tile = (towerTile) tileObj;
											Tower tower = tile.getTower();
											Enemy target = null;
											double minDist = Double.MAX_VALUE;
											for (Enemy enemy : engine.getActiveEnemies()) {
												double dx = tower.getCol() - enemy.getCol();
												double dy = tower.getRow() - enemy.getRow();
												double dist = Math.sqrt(dx * dx + dy * dy);
												if (dist <= tower.getRange() && dist < minDist) {
													minDist = dist;
													target = enemy;
												}
											}

											if (target != null) {
												Long lastFire = towerLastFireTimes.getOrDefault(tower, 0L);
												long cooldown = (long)((1_000_000_000 / tower.getFireRate())*3.5*speedMultiplier);
												if (now2 - lastFire >=cooldown) {
													fireFromTower(tower, target);
													towerLastFireTimes.put(tower, now2);
												}
											}
										}
									}
								}
							}
						}

						renderEnemies();
						renderGoldBags();
						updateHUD();

						if(gameSpeed) {
							engine.update();
							renderEnemies();
							renderGoldBags();
							updateHUD();
						}
						List<ProjectileController> toRemove = new ArrayList<>();
						for (ProjectileController proj : activeProjectiles) {
							proj.update(deltaTime); // pass deltaTime
							if (proj.isRemovable()) {
								toRemove.add(proj);
								mapGrid.getChildren().remove(proj);
							}
						}
						activeProjectiles.removeAll(toRemove);
						List<Enemy> toRemoveEnemies = new ArrayList<>();
						for (Enemy e : engine.getActiveEnemies()) {
							if (e.isDead()) {
								toRemoveEnemies.add(e);
								player.addGold(e.getReward());
								ImageView view = enemyViews.remove(e); 
								ProgressBar hp = enemyHP.remove(e);
								ImageView thunder = enemyThunder.remove(e);
								ImageView slow = enemySlow.remove(e);
								if (view != null) mapGrid.getChildren().remove(view);
								if (hp != null) mapGrid.getChildren().remove(hp);
								if (thunder != null) mapGrid.getChildren().remove(thunder);
								if (slow != null) mapGrid.getChildren().remove(slow);
								engine.maybeDropGoldBag(e, new Archer_Tower().getCost());
							}
						}
						engine.getActiveEnemies().removeAll(toRemoveEnemies);

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

		private void renderEnemies() { // renders enemy images and animation 
			for (Enemy e : engine.getActiveEnemies()) {
				ImageView view = enemyViews.get(e);
				ProgressBar HP = enemyHP.get(e);
				ImageView thunder = enemyThunder.get(e);
				ImageView slow = enemySlow.get(e);

				if (view == null) {
					startcol=e.getCol() ;
					startrow=e.getRow() ;
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
				
					ImageView thunderIcon = new ImageView(new Image(getClass().getResourceAsStream("/ui/Assets/thunder icon.png")));
					thunderIcon.setFitWidth(20);
					thunderIcon.setFitHeight(20);
					thunderIcon.setTranslateX(e.getCol() * TILE_WIDTH + TILE_WIDTH * 0.7);
					thunderIcon.setTranslateY(e.getRow() * TILE_HEIGHT - 30);
					thunderIcon.setVisible(e.hasCombatSynergy);
					mapGrid.getChildren().add(thunderIcon);
					enemyThunder.put(e, thunderIcon);
					
					ImageView slowIcon = new ImageView(new Image(getClass().getResourceAsStream("/ui/Assets/snow flake icon.png")));
					slowIcon.setFitWidth(20);
					slowIcon.setFitHeight(20);
					slowIcon.setTranslateX(e.getCol() * TILE_WIDTH - TILE_WIDTH * 0.3);
					slowIcon.setTranslateY(e.getRow() * TILE_HEIGHT - 30);
					slowIcon.setVisible(e.isSlowed());
					mapGrid.getChildren().add(slowIcon);
					enemySlow.put(e, slowIcon);
						
					

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
					slow.setVisible(e.isSlowed());
					thunder.setVisible(e.hasCombatSynergy);
					TranslateTransition ht = new TranslateTransition(Duration.millis(250), slow);
					TranslateTransition tt = new TranslateTransition(Duration.millis(250), view);
					TranslateTransition hh = new TranslateTransition(Duration.millis(250), HP);
					TranslateTransition th = new TranslateTransition(Duration.millis(250), thunder);
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
					th.setByX(movx);
					ht.setByX(movx);
					tt.setByY(movy);
					hh.setByY(movy);
					th.setByY(movy);
					ht.setByY(movy);
					tt.play();
					hh.play();
					th.play();
					ht.play();
					HP.setProgress((double)e.getHealth() / e.maxHP);
					
				}
				
			}
			for (Enemy e : enemyViews.keySet()) {
				if (!engine.getActiveEnemies().contains(e)) {
					mapGrid.getChildren().remove(enemyViews.get(e));
					mapGrid.getChildren().remove(enemyHP.get(e));
					mapGrid.getChildren().remove(enemyThunder.get(e));
					mapGrid.getChildren().remove(enemySlow.get(e));
					engine.maybeDropGoldBag(e, new Archer_Tower().getCost());
				}
			}
		}/* Specifications:
		  Requires:
		  -tower and target must not be null
		  -tower must have a valid level and damage
		  -getClass().getResourceAsStream(...) must return valid image streams
		  -engine and mapGrid must be initialized
		  -startrow and startcol must be within the map bounds
		 
		  Modifies:
		  -activeProjectiles: Adds a new ProjectileController
		  -mapGrid: Adds the controller node to the visual grid
		 
		  Effects:
		  -Creates a new Projectile based on the tower and target
		  -Selects an animation frame sequence based on the type and level of the tower
		  -Initializes a ProjectileController with the projectile and frames
		  -Sets its row, column, active enemies
		  -Adds the controller to both the `activeProjectiles` list and `mapGrid` for rendering and game logic
		 */
		private void fireFromTower(Tower tower, Enemy target) { // creates the projectile and image and calls its controller
			Projectile p = new Projectile(tower, target, tower.getDamage()); 
			
			Image[] fireFrames = new Image[] {
					new Image(getClass().getResourceAsStream("/ui/Assets/fireball_1.png")),
					new Image(getClass().getResourceAsStream("/ui/Assets/fireball_2.png")),
					new Image(getClass().getResourceAsStream("/ui/Assets/fireball_3.png")),
					new Image(getClass().getResourceAsStream("/ui/Assets/fireball_4.png")),
					new Image(getClass().getResourceAsStream("/ui/Assets/fireball_5.png")),
					new Image(getClass().getResourceAsStream("/ui/Assets/fireball_6.png"))
			};
			if (tower instanceof Archer_Tower) {
				fireFrames= new Image[] {
						new Image(getClass().getResourceAsStream("/ui/Assets/arrow.png"))
						
				};
			}
			if (tower instanceof Artillery_Tower) {
				fireFrames= new Image[] {
						new Image(getClass().getResourceAsStream("/ui/Assets/bomb.png"))
						
				};
			}
			if(tower instanceof Mage_Tower && tower.getLevel()==2) {
				fireFrames = new Image[] {
						new Image(getClass().getResourceAsStream("/ui/Assets/blue_fireball_1.png")),
						new Image(getClass().getResourceAsStream("/ui/Assets/blue_fireball_2.png")),
						new Image(getClass().getResourceAsStream("/ui/Assets/blue_fireball_3.png")),
						new Image(getClass().getResourceAsStream("/ui/Assets/blue_fireball_4.png")),
						new Image(getClass().getResourceAsStream("/ui/Assets/blue_fireball_5.png")),
						new Image(getClass().getResourceAsStream("/ui/Assets/blue_fireball_6.png"))
				};
			}
			ProjectileController controller = new ProjectileController(p, fireFrames);
			controller.setStartcol(startcol);
			controller.setStartrow(startrow);
			controller.setActiveEnemies(engine.getActiveEnemies());
			activeProjectiles.add(controller);
			mapGrid.getChildren().add(controller);
		}


		private void updateHUD() { //uptades the hud
	        numHealth.setText("" + player.getLives());
	        numCoin.setText("" + player.getGold());
	        numWave.setText(engine.getWaveNumber()+"/"+engine.getMaxWaves());
	    }
		@FXML
		private void handleBack() { // return to the map selector screen
			isPaused=true;
			player.setGold(defaultCoins);
			player.setLives(defaultHealth);
			Stage stage = (Stage)back.getScene().getWindow();
			stage.setScene(previousScene); 
			stage.setTitle("Main Menu");
	        stage.show();
		}
		private boolean isEdge(int row, int col) { // checks if it is edge
	        return row == 0 || row == ROWS - 1 || col == 0 || col == COLS - 1;
	    }
		private boolean isInside(int row, int col) {//checks if cords is inside the map
	        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
	    }
		
		private List<int[]> findPath(){// finds path by using recursin and deapth first search
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
		private void toggleSpeed() {// toggle game speed
			
			gameSpeed=!gameSpeed;
			if(gameSpeed) {
				speedMultiplier = 0.5; 
				toggle.setText("FAST");
			}
			else {
				speedMultiplier = 1; 
				toggle.setText("NORMAL");
			}
		}
		@FXML
		private void fireballMode() {
			if (fireballOnCooldown) {
				fireball.setText("COOLDOWN");
				return;
			}
			fireballMode = true;
			fireball.setText("FIREBALL ACTIVE");
			fireball.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: red; -fx-font-weight: bold; -fx-border-color: red; -fx-border-width: 3;");
			stopFireballPulse();
		}
		private void towerTileAction(int r,int c,Button tile) {// for sell or upgrade tower
			ContextMenu menu = new ContextMenu();
			MenuItem upgrade = new MenuItem("Upgrade");
			MenuItem sell = new MenuItem("Sell");
			upgrade.setOnAction(e ->{
				towerTile twrTile= (towerTile) map.getTile(r, c);
				Tower tower= twrTile.getTower();
				if(tower.getLevel()==2) {
					Alert alert = new Alert(AlertType.INFORMATION,"This tower is already upgraded!!!!!");
					alert.showAndWait();
				}
				
				else if(player.canAfford(tower.getCost())) {
					tower.upgradeTower();
					if(tower instanceof Mage_Tower) {
						tile.setStyle("-fx-background-image: url('" + getClass().getResource("assets/mageTowerUp.png") + "');" +
	            "-fx-background-size: cover;");
					}
					else if(tower instanceof Archer_Tower) {
						tile.setStyle("-fx-background-image: url('" + getClass().getResource("assets/archerTowerUp.png") + "');" +
	            "-fx-background-size: cover;");
					}
					else if(tower instanceof Artillery_Tower) {
						tile.setStyle("-fx-background-image: url('" + getClass().getResource("assets/artilleryTowerUp.png") + "');" +
	            "-fx-background-size: cover;");
					}
				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION,"You can't afford that!!!!!");
					alert.showAndWait();
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
					tower.setRow(r);
					tower.setCol(c);

				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION,"You can't afford that!!!!!");
					alert.showAndWait();
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
					tower.setRow(r);
					tower.setCol(c);

				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION,"You can't afford that!!!!!");
					alert.showAndWait();
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
					tower.setRow(r);
					tower.setCol(c);

				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION,"You can't afford that!!!!!");
					alert.showAndWait();
				}
				
				
		    });
			
			menu.getItems().addAll(archer, mage, artillery);
			menu.show(tile, Side.RIGHT, 0, 0);

		}
		private void renderGoldBags() {
		    Iterator<GoldBag> it = engine.getGoldBags().iterator();
		    while (it.hasNext()) {
		        GoldBag bag = it.next();
		        if (System.currentTimeMillis() - bag.getSpawnTime() > 10000) {
		            ImageView oldView = goldBagViews.get(bag);
		            if (oldView != null) {
		                mapGrid.getChildren().remove(oldView);
		            }
		            goldBagViews.remove(bag);
		            it.remove();
		        }
		    }
		    for (GoldBag bag : engine.getGoldBags()) {
		        if (!goldBagViews.containsKey(bag)) {
		            Image goldBagSheet = new Image(getClass().getResourceAsStream("/ui/Assets/G_Spawn.png"));
		            ImageView bagView = new ImageView(goldBagSheet);
		            int frameWidth = 128;
		            int frameHeight = 128;
		            int totalFrames = 7;
		            bagView.setFitWidth(55);
		            bagView.setFitHeight(55);
		            bagView.setTranslateX(bag.getX());
		            bagView.setTranslateY(bag.getY());
		            Timeline animation = new Timeline(new KeyFrame(Duration.millis(100), event -> {
		                int frame = (int)((System.currentTimeMillis() / 100) % totalFrames);
		                bagView.setViewport(new javafx.geometry.Rectangle2D(frame * frameWidth, 0, frameWidth, frameHeight));
		            }));
		            animation.setCycleCount(Timeline.INDEFINITE);
		            animation.play();
		            bagView.setOnMouseClicked(e -> {
		                player.addGold(bag.getAmount());
		                updateHUD();
		                mapGrid.getChildren().remove(bagView);
		                goldBagViews.remove(bag);
						int col = (int)(bag.getX() / TILE_WIDTH);
						int row = (int)(bag.getY() / TILE_HEIGHT);
						showMoneyAnimation(col, row, bag.getAmount());
		                engine.removeGoldBag(bag);
		                animation.stop();
		            });
		            goldBagViews.put(bag, bagView);
		            mapGrid.getChildren().add(bagView);
		        }
		    }
		}
		private void spawnRandomWoods() {
			woods.clear();
			Random rand = new Random();
			int numWoods = rand.nextInt(5) + 5;

			List<int[]> grassTiles = new ArrayList<>();
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					if (currentMap[row][col] == TileType.GRASS) {
						grassTiles.add(new int[]{row, col});
					}
				}
			}
			Collections.shuffle(grassTiles, rand);

			for (int i = 0; i < Math.min(numWoods, grassTiles.size()); i++) {
				int[] pos = grassTiles.get(i);
				woods.add(new Wood(pos[0], pos[1], 5)); // 5 coins per wood
			}
		}

		private void renderWoods() {
			// Remove old wood views
			for (ImageView view : woodViews.values()) {
				mapGrid.getChildren().remove(view);
			}
			woodViews.clear();

			for (Wood wood : woods) {
				Image woodImage = new Image(getClass().getResourceAsStream("/ui/Assets/wood.png"));
				ImageView woodView = new ImageView(woodImage);
				woodView.setFitWidth(40);
				woodView.setFitHeight(40);
				woodView.setTranslateX(wood.getCol() * TILE_WIDTH + 15);
				woodView.setTranslateY(wood.getRow() * TILE_HEIGHT + 5);

				woodView.setOnMouseClicked(e -> {
					player.addGold(wood.getAmount());
					updateHUD();
					showMoneyAnimation(wood.getCol(), wood.getRow(), wood.getAmount());
					mapGrid.getChildren().remove(woodView);
					woodViews.remove(wood);
					woods.remove(wood);
				});

				woodViews.put(wood, woodView);
				mapGrid.getChildren().add(woodView);
			}
		}
		private void showMoneyAnimation(int col, int row, int amount) {
			Label moneyLabel = new Label("+" + amount);
			moneyLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: gold; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 2, 0, 1, 1);");
			moneyLabel.setTranslateX(col * TILE_WIDTH + 25);
			moneyLabel.setTranslateY(row * TILE_HEIGHT + 10);

			mapGrid.getChildren().add(moneyLabel);

			TranslateTransition moveUp = new TranslateTransition(Duration.millis(1500), moneyLabel);
			moveUp.setByY(-30);

			FadeTransition fade = new FadeTransition(Duration.millis(1500), moneyLabel);
			fade.setFromValue(1.0);
			fade.setToValue(0.0);

			ParallelTransition animation = new ParallelTransition(moveUp, fade);
			animation.setOnFinished(ev -> mapGrid.getChildren().remove(moneyLabel));
			animation.play();
		}
		private void startFireballPulse() {
			if (fireballPulse != null) fireballPulse.stop();
			fireballPulse = new ScaleTransition(Duration.millis(800), fireball);
			fireballPulse.setFromX(1.0);
			fireballPulse.setFromY(1.0);
			fireballPulse.setToX(1.15);
			fireballPulse.setToY(1.15);
			fireballPulse.setAutoReverse(true);
			fireballPulse.setCycleCount(Animation.INDEFINITE);
			fireballPulse.play();
		}

		private void stopFireballPulse() {
			if (fireballPulse != null) {
				fireballPulse.stop();
				fireball.setScaleX(1.0);
				fireball.setScaleY(1.0);
			}
		}

	}
