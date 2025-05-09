package ui;

import domain.Projectile;
import domain.Enemy;
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

    public ProjectileController(Projectile projectile, Image[] fireFrames) {
        super(fireFrames[0]); // Start with first frame
        this.projectile = projectile;
        this.target = projectile.getTarget();
        this.frames = fireFrames;

        this.setFitWidth(30);
        this.setFitHeight(30);
        this.setTranslateX(projectile.getSource().getCol() * 75);
        this.setTranslateY(projectile.getSource().getRow() * 50);

        startAnimation();
    }

    private void startAnimation() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(80), e -> {
            int frameIndex = (int)((System.currentTimeMillis() / 80) % frames.length);
            this.setImage(frames[frameIndex]);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public boolean update() {
        if (hasHit || target == null || target.isDead()) return true;

        double dx = target.getxCoordinate() - getTranslateX();
        double dy = target.getyCoordinate() - getTranslateY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        //Rotating the fireball accordingly
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        this.setRotate(angle);

        if (dist < 10) {
            if (Math.random() > 0.1) {
                target.takeDamage(projectile.getDamage());
            }
            hasHit = true;
            return true;
        }

        double speed = 10;
        setTranslateX(getTranslateX() + (dx / dist) * speed);
        setTranslateY(getTranslateY() + (dy / dist) * speed);

        return false;
    }
}
