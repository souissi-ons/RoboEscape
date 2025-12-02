package roboescape.model.items;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import roboescape.model.player.Player;
import roboescape.patterns.decorator.PowerUp;

public class PowerUpItem extends Item {
    private PowerUp powerUp;
    private Color color;

    public PowerUpItem(double x, double y, PowerUp powerUp, Color color) {
        super(x, y);
        this.powerUp = powerUp;
        this.color = color;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;
        gc.setFill(color);
        gc.fillOval(x, y, size, size);
        gc.setStroke(Color.WHITE);
        gc.strokeOval(x, y, size, size);
    }

    @Override
    public void update() {
        // Animation simple (pulsation) possible ici
    }

    @Override
    public void onCollect(Player p) {
        p.addPowerUp(powerUp);
        this.active = false; // Disparaît
        System.out.println("Item Collected!");
    }
}