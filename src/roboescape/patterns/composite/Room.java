package roboescape.patterns.composite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Room implements LevelComponent {

    private double x, y, width, height;

    public Room(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(3);
        gc.strokeRect(x, y, width, height);
    }

    @Override
    public void update() {
    }
}
