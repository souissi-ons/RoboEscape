package roboescape.patterns.composite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import roboescape.model.player.Player;

public class Exit implements LevelComponent {
    private double x, y, size;

    public Exit(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    @Override
    public void render(GraphicsContext gc) {
        // Dessin d'une "Porte" ou "Portail"
        
        // Fond noir (le vide)
        gc.setFill(Color.BLACK);
        gc.fillRect(x, y, size, size);
        
        // Spirale ou cercles pour faire "Portail"
        gc.setStroke(Color.MAGENTA);
        gc.setLineWidth(3);
        gc.strokeOval(x + 5, y + 5, size - 10, size - 10);
        gc.setStroke(Color.VIOLET);
        gc.strokeOval(x + 15, y + 15, size - 30, size - 30);
        
        // Texte "EXIT"
        gc.setFill(Color.WHITE);
        gc.fillText("EXIT", x + 10, y + size / 2 + 5);
    }

    @Override
    public void update() {
        // Animation possible ici (faire tourner les cercles)
    }

    // Détection si le joueur atteint la sortie
    public boolean checkCollision(Player p) {
        return p.getX() < x + size && p.getX() + p.getSize() > x &&
               p.getY() < y + size && p.getY() + p.getSize() > y;
    }
}