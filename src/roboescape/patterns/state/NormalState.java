package roboescape.patterns.state;

import javafx.scene.paint.Color;
import roboescape.model.player.Player;

public class NormalState implements PlayerState {

    private final Player player;

    public NormalState(Player p) {
        this.player = p;
        player.setSpeed(3);
    }

    @Override
    public void update() {
        // Logique normale
    }

    @Override
    public void onEnter() {
        // Logique d'entrée (ex: jouer un son)
    }

    @Override
    public Color getColor() {
        return Color.DODGERBLUE; // Retourne la couleur bleue
    }
}