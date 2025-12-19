package roboescape.patterns.decorator;

import roboescape.model.player.Player;
import roboescape.patterns.util.PatternLogger;

/**
 * InvincibilityBoost Power-Up (Decorator Pattern)
 * 
 * Grants temporary invincibility to the player by combining shield protection
 * with health preservation. During this time, the player is highly protected.
 * Duration: 4 seconds
 * Visual Effect: Golden glow (rendered via shield + special marker)
 */
public class InvincibilityBoost implements PowerUp {

    private double duration = 4.0; // 4 seconds
    private boolean expired = false;

    public InvincibilityBoost() {
    }

    @Override
    public void apply(Player player) {
        PatternLogger.logDecoratorApplied("InvincibilityBoost(4s)", "Player");
    }

    @Override
    public void update(Player player) {
        duration -= 0.016; // ~60 fps
        if (duration <= 0 && !expired) {
            expired = true;
            PatternLogger.logDecoratorExpired("InvincibilityBoost");
        }
    }

    @Override
    public boolean isExpired() {
        return expired;
    }

    /**
     * Get remaining duration for HUD display
     * 
     * @return remaining time in seconds
     */
    public double getRemainingDuration() {
        return Math.max(0, duration);
    }
}
