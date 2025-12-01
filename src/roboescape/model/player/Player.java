package roboescape.model.player;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import roboescape.patterns.decorator.PowerUp;
import roboescape.patterns.state.PlayerState;
import roboescape.patterns.state.NormalState;

public class Player {

    private double x = 100;
    private double y = 100;
    private double size = 40;

    private double speed = 3;
    private boolean shield = false;

    private PlayerState currentState;
    private List<PowerUp> powerUps = new ArrayList<>();

    public Player() {
        this.currentState = new NormalState(this);
    }

    // -----------------------------------------
    // UPDATE
    // -----------------------------------------
    public void update(double width, double height) {

        currentState.update();

        // update power-ups
        powerUps.forEach(p -> p.update(this));
        powerUps.removeIf(PowerUp::isExpired);

        // boundaries
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + size > width) x = width - size;
        if (y + size > height) y = height - size;
    }

    // -----------------------------------------
    // ADD POWERUP
    // -----------------------------------------
    public void addPowerUp(PowerUp p) {
        p.apply(this);
        powerUps.add(p);
    }

    // -----------------------------------------
    // RENDER
    // -----------------------------------------
    public void render(GraphicsContext gc) {

        // PLAYER BODY
        gc.setFill(currentState instanceof roboescape.patterns.state.PoweredUpState
                ? Color.GOLD : Color.DODGERBLUE);

        gc.fillRect(x, y, size, size);

        // SHIELD HALO
        if (shield) {
            gc.setStroke(Color.LIGHTBLUE);
            gc.setLineWidth(4);
            gc.strokeOval(x - 8, y - 8, size + 16, size + 16);
        }

        // HUD (affiche vitesse)
        gc.setFill(Color.WHITE);
        gc.fillText("Speed : " + speed, 10, 20);
    }

    // -----------------------------------------
    // MOVEMENT
    // -----------------------------------------
    public void move(double dx, double dy) {
        this.x += dx * speed;
        this.y += dy * speed;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getSize() { return size; }

    public double getSpeed() { return speed; }
    public void setSpeed(double s) { speed = s; }

    public void enableShield() { shield = true; }
    public void disableShield() { shield = false; }
    public boolean hasShield() { return shield; }

    public void setCurrentState(PlayerState s) { currentState = s; }
}
