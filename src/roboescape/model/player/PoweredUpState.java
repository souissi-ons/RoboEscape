package roboescape.model.player;

public class PoweredUpState implements PlayerState {

    private final Player player;
    private long startTime;
    private long duration = 3000; // 3 secondes

    public PoweredUpState(Player p) {
        this.player = p;
        this.startTime = System.currentTimeMillis();
        player.setSpeed(6);
    }

    @Override
    public void update() {
        long now = System.currentTimeMillis();
        if (now - startTime > duration) {
            player.setState(new NormalState(player));
        }
    }
}
