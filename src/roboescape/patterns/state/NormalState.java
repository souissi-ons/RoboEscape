package roboescape.patterns.state;

import roboescape.model.player.Player;

public class NormalState implements PlayerState {

    private final Player player;

    public NormalState(Player p) {
        this.player = p;
        player.setSpeed(3);
    }

    @Override
    public void update() {
        // Rien de spécial ici
    }
}
