package roboescape.patterns.decorator;

import roboescape.model.player.Player;
import roboescape.patterns.util.PatternLogger;

public class SpeedBoost implements PowerUp {

    private double duration = 5.0; // 5 sec
    private final double bonus = 2.0;
    private boolean expired = false;

    public SpeedBoost() {
    }

    @Override
    public void apply(Player player) {
        player.setSpeed(player.getSpeed() + bonus);
        PatternLogger.logDecoratorApplied("SpeedBoost(+" + bonus + ")", "Player");
    }

    @Override
    public void update(Player player) {
        duration -= 0.016; // ~60 fps
        if (duration <= 0 && !expired) {
            player.setSpeed(player.getSpeed() - bonus);
            expired = true;
            PatternLogger.logDecoratorExpired("SpeedBoost");
        }
    }

    @Override
    public boolean isExpired() {
        return expired;
    }
}
