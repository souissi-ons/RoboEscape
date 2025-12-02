package roboescape.model.items;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import roboescape.model.player.Player;

public class TrapItem extends Item {
    public TrapItem(double x, double y) { super(x, y); }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;
        gc.setFill(Color.DARKRED);
        gc.fillOval(x + 2, y + 10, 16, 5); // Base
        gc.fillPolygon(new double[]{x+5, x+10, x+15}, new double[]{y+10, y, y+10}, 3); // Pique
    }

    @Override
    public void update() {}

    @Override
    public void onCollect(Player p) {
        if (!p.hasShield()) {
            p.takeDamage(1);
            System.out.println("BOOM ! Piège activé.");
        }
        this.active = false; // Le piège disparait après explosion
    }
}