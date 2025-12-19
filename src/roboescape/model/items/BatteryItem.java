package roboescape.model.items;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import roboescape.model.player.Player;

public class BatteryItem extends Item {

    public BatteryItem(double x, double y) {
        super(x, y);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active)
            return;

        // --- BATTERY CYLINDER DESIGN ---
        double w = 12;
        double h = 20;
        double cx = x + 5; // Centered X offset

        // 1. Body (Metallic/Dark Grey Container)
        gc.setFill(Color.rgb(50, 50, 60));
        gc.fillRoundRect(cx, y, w, h, 4, 4);
        gc.setStroke(Color.rgb(30, 30, 40));
        gc.setLineWidth(1);
        gc.strokeRoundRect(cx, y, w, h, 4, 4);

        // 2. Cap (Silver Pole)
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(cx + 3, y - 3, 6, 3);
        gc.setStroke(Color.GRAY);
        gc.strokeRect(cx + 3, y - 3, 6, 3);

        // 3. Core (Glowing Energy)
        // Draw a smaller rect inside representing the charge
        gc.setFill(Color.rgb(0, 255, 100)); // Vivid Green
        gc.fillRoundRect(cx + 2, y + 2, w - 4, h - 4, 2, 2);

        // 4. Shine/Glare (Glassy effect)
        gc.setFill(Color.rgb(255, 255, 255, 0.4));
        gc.fillRoundRect(cx + 2, y + 2, 3, h - 4, 2, 2);

        // 5. Symbol (Centered +)
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        // Manual centering based on font size approx
        gc.fillText("+", cx + 1.5, y + 15);
        gc.strokeText("+", cx + 1.5, y + 15);
    }

    // VOICI LA MÉTHODE QUI MANQUAIT
    @Override
    public void update() {
        // Animation simple : rien à faire pour l'instant, ou faire flotter l'objet
    }

    @Override
    public void onCollect(Player p) {
        p.heal(1); // +1 Vie
        p.addScore(50); // +50 Points
        this.active = false;
        System.out.println("Batterie collectee ! PV : " + p.getHealth());
    }
}