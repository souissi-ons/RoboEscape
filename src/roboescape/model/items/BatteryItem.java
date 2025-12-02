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
        if (!active) return;
        
        // Dessin d'une petite pile verte
        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(x + 5, y, 10, 20); // Corps
        gc.setFill(Color.GRAY);
        gc.fillRect(x + 8, y - 3, 4, 3); // Tête (+ pole)
        
        // Petit éclair blanc pour le style
        gc.setFill(Color.WHITE);
        gc.fillText("+", x+6, y+15);
    }

    // VOICI LA MÉTHODE QUI MANQUAIT
    @Override
    public void update() {
        // Animation simple : rien à faire pour l'instant, ou faire flotter l'objet
    }

    @Override
    public void onCollect(Player p) {
        p.heal(1);      // +1 Vie
        p.addScore(50); // +50 Points
        this.active = false;
        System.out.println("Batterie collectée ! PV : " + p.getHealth());
    }
}