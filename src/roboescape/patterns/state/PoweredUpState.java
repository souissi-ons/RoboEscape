package roboescape.patterns.state;

import roboescape.model.player.Player;

public class PoweredUpState implements PlayerState {

    private Player player;
    private long startTime;

    public PoweredUpState(Player player) {
        this.player = player;

        player.setSpeed(7);
        player.enableShield();

        startTime = System.currentTimeMillis();
    }

    @Override
    public void update() {

        long elapsed = System.currentTimeMillis() - startTime;

        if (elapsed > 3000) {  // 3 seconds
            player.setSpeed(3);
            player.disableShield();
            player.setCurrentState(new NormalState(player));
        }
    }
}
