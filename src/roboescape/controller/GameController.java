package roboescape.controller;

import javafx.scene.input.KeyCode;
import roboescape.model.player.Player;

import roboescape.patterns.state.PoweredUpState;
import roboescape.patterns.decorator.SpeedBoost;
import roboescape.patterns.decorator.ShieldBoost;

public class GameController {

    private boolean up, down, left, right;
    private final Player player;

    public GameController(Player p) {
        this.player = p;
    }

    public void update(double width, double height) {
        double dx = 0;
        double dy = 0;

        if (up) dy -= 1;
        if (down) dy += 1;
        if (left) dx -= 1;
        if (right) dx += 1;

        player.move(dx, dy);
        player.update(width, height);
    }

    public void onKeyPressed(KeyCode code) {
        switch (code) {

            case UP -> up = true;
            case DOWN -> down = true;
            case LEFT -> left = true;
            case RIGHT -> right = true;

            case SPACE -> player.setCurrentState(new PoweredUpState(player)); // STATE PATTERN
            case S -> player.addPowerUp(new SpeedBoost()); // DECORATOR
            case D -> player.addPowerUp(new ShieldBoost());
        }
    }

    public void onKeyReleased(KeyCode code) {
        switch (code) {
            case UP -> up = false;
            case DOWN -> down = false;
            case LEFT -> left = false;
            case RIGHT -> right = false;
        }
    }
}
