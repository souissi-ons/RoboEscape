package roboescape.patterns.decorator;

import roboescape.model.player.Player;

public class SpeedBoost implements PowerUp {

    private double duration = 5.0;   // 5 sec
    private final double bonus = 2.0;
    private boolean expired = false;

    public SpeedBoost() {}

    @Override
    public void apply(Player player) {
        player.setSpeed(player.getSpeed() + bonus);
        System.out.println("Speed Boost applied +2 !");
    }

    @Override
    public void update(Player player) {
        duration -= 0.016;  // ~60 fps
        if (duration <= 0 && !expired) {
            player.setSpeed(player.getSpeed() - bonus);
            expired = true;
            System.out.println("Speed Boost expired.");
        }
    }

    @Override
    public boolean isExpired() {
        return expired;
    }
}
