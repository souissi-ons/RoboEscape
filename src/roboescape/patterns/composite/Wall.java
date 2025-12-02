package roboescape.patterns.composite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wall implements LevelComponent {
    private double x, y, width, height;

    public Wall(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(GraphicsContext gc) {
        // Dessin style "Brique" ou Métal
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x, y, width, height);
        
        // Bordure pour l'effet 3D
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, width, height);
    }

    @Override
    public void update() {
        // Les murs ne bougent pas
    }

    // Pour la détection de collision
    // Dans Wall.java
public boolean intersects(double px, double py, double pSize) {
    // AABB Collision (Axis-Aligned Bounding Box)
    return px < x + width &&        // Le côté gauche du joueur est à gauche du côté droit du mur
           px + pSize > x &&        // Le côté droit du joueur est à droite du côté gauche du mur
           py < y + height &&       // Le haut du joueur est au-dessus du bas du mur
           py + pSize > y;          // Le bas du joueur est en dessous du haut du mur
}
}