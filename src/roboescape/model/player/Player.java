package roboescape.model.player;

public class Player {

    private PlayerState state;

    public Player() {
        this.state = new NormalState();
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void move() {
        state.move();
    }
}
