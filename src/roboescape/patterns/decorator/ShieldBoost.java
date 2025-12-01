package roboescape.patterns.decorator;

import roboescape.model.player.Player;

public class ShieldBoost implements PowerUp {

    private double duration = 5.0;
    private boolean expired = false;

    public ShieldBoost() {}

    @Override
    public void apply(Player player) {
        player.enableShield();
        System.out.println("Shield activated !");
    }

    @Override
    public void update(Player player) {
        duration -= 0.016;
        if (duration <= 0 && !expired) {
            player.disableShield();
            expired = true;
            System.out.println("Shield expired.");
        }
    }

    @Override
    public boolean isExpired() {
        return expired;
    }
}
