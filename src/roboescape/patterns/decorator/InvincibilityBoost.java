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
    private boolean hadShield = false;
    private int healthSnapshot = 0;

    public InvincibilityBoost() {
    }

    @Override
    public void apply(Player player) {
        // Store current shield state
        hadShield = player.hasShield();

        // Store current health to restore if damaged during invincibility
        healthSnapshot = player.getHealth();

        // Enable shield for visual effect and protection
        player.enableShield();

        PatternLogger.logDecoratorApplied("InvincibilityBoost(4s)", "Player");
    }

    @Override
    public void update(Player player) {
        // Restore health if player took damage during invincibility
        if (player.getHealth() < healthSnapshot && !expired) {
            player.heal(healthSnapshot - player.getHealth());
        }

        duration -= 0.016; // ~60 fps
        if (duration <= 0 && !expired) {
            // Only disable shield if player didn't have it before
            if (!hadShield) {
                player.disableShield();
            }
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
