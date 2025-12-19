package roboescape.patterns.decorator;

import roboescape.model.player.Player;
import roboescape.patterns.util.PatternLogger;

public class ShieldBoost implements PowerUp {

    private double duration = 5.0;
    private boolean expired = false;

    public ShieldBoost() {
    }

    @Override
    public void apply(Player player) {
        player.enableShield();
        PatternLogger.logDecoratorApplied("ShieldBoost(5s)", "Player");
    }

    @Override
    public void update(Player player) {
        duration -= 0.016;
        if (duration <= 0 && !expired) {
            player.disableShield();
            expired = true;
            PatternLogger.logDecoratorExpired("ShieldBoost");
        }
    }

    @Override
    public boolean isExpired() {
        return expired;
    }
}
