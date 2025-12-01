package roboescape.patterns.decorator;

import roboescape.model.player.Player;

public abstract class PlayerDecorator extends Player {

    protected Player player;

    public PlayerDecorator(Player player) {
        this.player = player;
    }

    @Override
    public double getSpeed() {
        return player.getSpeed();
    }

    @Override
    public boolean hasShield() {
        return player.hasShield();
    }

    @Override
    public void update(double w, double h) {
        player.update(w, h);
    }

    @Override
    public void render(javafx.scene.canvas.GraphicsContext gc) {
        player.render(gc);
    }
}
