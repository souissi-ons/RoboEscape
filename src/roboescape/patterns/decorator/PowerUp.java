package roboescape.patterns.decorator;

import roboescape.model.player.Player;

public interface PowerUp {
    void apply(Player player);
    void update(Player player);   // pour timers
    boolean isExpired();
}
