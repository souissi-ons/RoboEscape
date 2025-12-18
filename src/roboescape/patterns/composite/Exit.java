package roboescape.patterns.composite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import roboescape.model.player.Player;

public class Exit implements LevelComponent {
    private double x, y, size;
    private double angle = 0; // For rotation animation

    public Exit(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    @Override
    public void render(GraphicsContext gc) {
        // --- PORTAL VORTEX DESIGN ---
        double cx = x + size / 2;
        double cy = y + size / 2;

        gc.save();

        // 1. Black Hole Core
        gc.setFill(Color.BLACK);
        gc.fillOval(x, y, size, size);

        // 2. Rotating Vortex Rings
        gc.translate(cx, cy);
        gc.rotate(angle); // Rotate the whole context
        gc.translate(-cx, -cy);

        // Ring 1 (Cyan)
        gc.setStroke(Color.rgb(0, 255, 255, 0.7));
        gc.setLineWidth(4);
        gc.strokeOval(x + 5, y + 5, size - 10, size - 10);

        // Ring 2 (Magenta - Offset)
        gc.setStroke(Color.rgb(255, 0, 255, 0.6));
        gc.setLineWidth(3);
        gc.strokeOval(x + 12, y + 12, size - 24, size - 24); // Slightly elliptical or smaller

        // Ring 3 (Inner White)
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        drawDashedCircle(gc, cx, cy, size / 2 - 15);

        // 3. Glow Center
        gc.setFill(Color.rgb(100, 100, 255, 0.5));
        gc.fillOval(cx - 5, cy - 5, 10, 10);

        gc.restore();
    }

    private void drawDashedCircle(GraphicsContext gc, double cx, double cy, double radius) {
        gc.setLineDashes(5);
        gc.strokeOval(cx - radius, cy - radius, radius * 2, radius * 2);
        gc.setLineDashes(null);
    }

    @Override
    public void update() {
        angle += 2; // Rotate 2 degrees per frame
        if (angle >= 360)
            angle = 0;
    }

    // Détection si le joueur atteint la sortie
    public boolean checkCollision(Player p) {
        return p.getX() < x + size && p.getX() + p.getSize() > x &&
                p.getY() < y + size && p.getY() + p.getSize() > y;
    }
}