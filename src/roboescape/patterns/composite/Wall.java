package roboescape.patterns.composite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wall implements LevelComponent {
    private double x, y, width, height;

    public Wall(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x, y, width, height);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, width, height);
    }

    @Override
    public void update() {}

    // Getters essentiels pour la physique
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    
    // Ancienne méthode (gardée pour compatibilité)
    public boolean intersects(double px, double py, double pSize) {
        return px < x + width && px + pSize > x &&
               py < y + height && py + pSize > y;
    }
}