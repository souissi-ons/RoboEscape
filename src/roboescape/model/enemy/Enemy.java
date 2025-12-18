package roboescape.model.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import roboescape.patterns.composite.LevelComponent;
import roboescape.patterns.strategy.MovementStrategy;

public class Enemy implements LevelComponent {

    private double x, y;
    private double size = 30;
    private double speed = 2;
    private MovementStrategy movementStrategy; // Le Pattern Strategy

    public Enemy(double x, double y, MovementStrategy strategy) {
        this.x = x;
        this.y = y;
        this.movementStrategy = strategy;
    }

    @Override
    public void update() {
        if (movementStrategy != null) {
            movementStrategy.move(this, speed);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // Dessin de l'ennemi (Rouge méchant)
        gc.setFill(Color.RED);
        gc.fillRect(x, y, size, size);

        // Yeux
        gc.setFill(Color.YELLOW);
        gc.fillOval(x + 5, y + 5, 8, 8);
        gc.fillOval(x + 18, y + 5, 8, 8);
    }

    // Hitbox pour collision avec le joueur
    public boolean checkCollision(double px, double py, double pSize) {
        return px < x + size && px + pSize > x
                && py < y + size && py + pSize > y;
    }

    // Getters/Setters pour la stratégie
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public MovementStrategy getMovementStrategy() {
        return movementStrategy;
    }

    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    private roboescape.patterns.composite.Level level;

    public void setLevel(roboescape.patterns.composite.Level level) {
        this.level = level;
    }

    public roboescape.patterns.composite.Level getLevel() {
        return level;
    }

}
