package ui;

import domain.Projectile;

import java.util.Random;

import domain.Enemy;
import domain.Mage_Tower;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class ProjectileController extends ImageView {
    private final Projectile projectile;
    private final Enemy target;
    private boolean hasHit = false;
    private final Image[] frames;
    private final double speed = 300; // pixels per second
    private Timeline animation;
    private Timeline impactTimeline;
    private final Image[] explosionFrames;
    private int currentExplosionFrame = 0;
    private boolean removable = false;
    private int startrow;
    private int startcol;

    public void setStartrow(int startrow) {
		this.startrow = startrow;
	}

	public void setStartcol(int startcol) {
		this.startcol = startcol;
	}

	public ProjectileController(Projectile projectile, Image[] fireFrames) {
        super(fireFrames[0]);
        this.projectile = projectile;
        this.target = projectile.getTarget();
        this.frames = fireFrames;
        this.explosionFrames = new Image[] {
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

        this.setFitWidth(30);
        this.setFitHeight(30);
        this.setTranslateX(projectile.getSource().getCol() * 75);
        this.setTranslateY(projectile.getSource().getRow() * 50);

        startAnimation();
    }

    private void startAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(80), e -> {
            int frameIndex = (int)((System.currentTimeMillis() / 80) % frames.length);
            this.setImage(frames[frameIndex]);
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    // New update method with deltaTime
    public void update(double deltaTime) {
        if (hasHit || target == null || target.isDead()) {
        	removable = true;
        	return;
        }
        	

        double dx = target.getxCoordinate() - getTranslateX();
        double dy = target.getyCoordinate() - getTranslateY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        double angle = Math.toDegrees(Math.atan2(dy, dx));
        this.setRotate(angle);

        if (dist < 10) {
            if (Math.random() > 0.1) {
                target.takeDamage(projectile.getDamage());
            }
            hasHit = true;
            showImpact();
            if (projectile.getSource() instanceof Mage_Tower) {
            	Random random = new Random();
            	int percent = random.nextInt(100);
            	if (percent<70) {
					target.setCol(startcol);
					target.setRow(startrow);
				}
				
			}
            removable = true;
            return;
        }

        double moveDist = Math.min(dist, speed * deltaTime);
        setTranslateX(getTranslateX() + (dx / dist) * moveDist);
        setTranslateY(getTranslateY() + (dy / dist) * moveDist);
    }

    private void showImpact() {
        if (animation != null) animation.stop();
        currentExplosionFrame = 0;
        this.setFitWidth(50);
        this.setFitHeight(50);
        impactTimeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (currentExplosionFrame < explosionFrames.length) {
                this.setImage(explosionFrames[currentExplosionFrame]);
                currentExplosionFrame++;
            } else {
                
                impactTimeline.stop();
            }
        }));
        impactTimeline.setCycleCount(Timeline.INDEFINITE);
        impactTimeline.play();
        
    }

    // Call this to check if the projectile is ready to be removed
    public boolean isRemovable() {
        return removable;
    }
}
