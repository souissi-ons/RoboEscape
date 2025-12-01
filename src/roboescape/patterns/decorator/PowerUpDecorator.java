package roboescape.patterns.decorator;

import roboescape.model.player.Player;

public abstract class PowerUpDecorator implements PowerUp {

    protected Player decoratedPlayer;

    public PowerUpDecorator(Player p) {
        this.decoratedPlayer = p;
    }
}
