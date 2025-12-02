package roboescape.model.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import roboescape.patterns.strategy.MovementStrategy;

public class BladeEnemy extends Enemy {

    private double angle = 0; // Pour l'animation de rotation

    public BladeEnemy(double x, double y, MovementStrategy strategy) {
        super(x, y, strategy);
    }

    @Override
    public void render(GraphicsContext gc) {
        // Mise à jour de la position via la stratégie (déplacement)
        super.update(); 
        
        // Animation : Rotation visuelle
        angle += 10; 
        
        double centerX = getX() + 15; // Centre (taille 30 / 2)
        double centerY = getY() + 15;

        gc.save(); // Sauvegarde l'état graphique
        gc.translate(centerX, centerY);
        gc.rotate(angle);
        
        // Dessin de la scie
        gc.setFill(Color.SILVER);
        gc.fillOval(-15, -15, 30, 30); // Disque principal
        
        gc.setStroke(Color.DARKRED);
        gc.setLineWidth(3);
        gc.strokeOval(-12, -12, 24, 24); // Cercle intérieur
        
        // Dents de la scie
        gc.setFill(Color.GRAY);
        gc.fillRect(-18, -2, 36, 4); // Dent H
        gc.fillRect(-2, -18, 4, 36); // Dent V
        
        gc.restore(); // Restaure l'état (pour ne pas tourner tout le jeu)
    }
}