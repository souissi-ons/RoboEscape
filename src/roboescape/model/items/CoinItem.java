package roboescape.model.items;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import roboescape.model.player.Player;

public class CoinItem extends Item {
    private double time = 0; // For bobbing animation

    public CoinItem(double x, double y) {
        super(x, y);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active)
            return;

        // Bobbing effect (Float up and down)
        double yOffset = Math.sin(time) * 3;
        double cx = x + 5;
        double cy = y + 5 + yOffset;
        double r = 6; // Radius

        // 1. Outer Gold Ring
        gc.setFill(Color.GOLD);
        gc.fillOval(cx - r, cy - r, r * 2, r * 2);

        // 2. Inner Light Gold
        gc.setFill(Color.rgb(255, 223, 0)); // Bright Yellow/Gold
        gc.fillOval(cx - (r - 1), cy - (r - 1), (r - 1) * 2, (r - 1) * 2);

        // 3. Border styling
        gc.setStroke(Color.rgb(218, 165, 32)); // Darker rod
        gc.setLineWidth(1);
        gc.strokeOval(cx - r, cy - r, r * 2, r * 2);

        // 4. Shine (White arc)
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1.5);
        gc.strokeArc(cx - r + 3, cy - r + 3, r, r, 90, 60, javafx.scene.shape.ArcType.OPEN);

        // 5. Letter/Symbol (Optional - maybe '$' or 'C')
        // gc.setFill(Color.rgb(180, 140, 0));
        // gc.setFont(javafx.scene.text.Font.font("Arial",
        // javafx.scene.text.FontWeight.BOLD, 10));
        // gc.fillText("$", cx - 2.5, cy + 3.5);
    }

    @Override
    public void update() {
        time += 0.1; // Animation speed
    }

    @Override
    public void onCollect(Player p) {
        p.addScore(100);
        this.active = false;
    }
}