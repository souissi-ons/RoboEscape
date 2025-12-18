package roboescape.patterns.state;

import javafx.scene.paint.Color;
import roboescape.model.player.Player;

public class PoweredUpState implements PlayerState {

    private Player player;
    private long startTime;

    public PoweredUpState(Player player) {
        this.player = player;
        player.setSpeed(800);
        player.enableShield();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed > 3000) { // 3 secondes
            player.setSpeed(400);
            player.disableShield();
            player.setCurrentState(new NormalState(player));
        }
    }

    @Override
    public void onEnter() {
        // Log "PowerUp Activated"
    }

    @Override
    public Color getColor() {
        return Color.GOLD; // Retourne la couleur dorée
    }
}