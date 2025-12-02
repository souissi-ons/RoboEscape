package roboescape.model.items;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import roboescape.model.player.Player;

public class CoinItem extends Item {
    public CoinItem(double x, double y) { super(x, y); }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;
        gc.setFill(Color.GOLD);
        gc.fillOval(x + 5, y + 5, 10, 10); // Pièce
        gc.setStroke(Color.ORANGE);
        gc.setLineWidth(1);
        gc.strokeOval(x + 5, y + 5, 10, 10);
    }

    @Override
    public void update() { /* Faire tourner la pièce plus tard ? */ }

    @Override
    public void onCollect(Player p) {
        p.addScore(100);
        this.active = false;
    }
}