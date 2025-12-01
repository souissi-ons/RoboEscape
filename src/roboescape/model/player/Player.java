package roboescape.model.player;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player {

    private double x = 100;
    private double y = 100;
    private double size = 40;

    private double speed = 3;

    private PlayerState currentState;

    public Player() {
        this.currentState = new NormalState(this);
    }

    public void update(double width, double height) {
        currentState.update();

        // Empêcher de sortir de l'écran
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + size > width) x = width - size;
        if (y + size > height) y = height - size;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.DODGERBLUE);
        gc.fillRect(x, y, size, size);
    }

    public void move(double dx, double dy) {
        this.x += dx * speed;
        this.y += dy * speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setState(PlayerState state) {
        this.currentState = state;
    }
}
