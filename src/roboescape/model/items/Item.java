package roboescape.model.items;

import javafx.scene.canvas.GraphicsContext;
import roboescape.model.player.Player;
import roboescape.patterns.composite.LevelComponent;

public abstract class Item implements LevelComponent {
    protected double x, y;
    protected double size = 20;
    protected boolean active = true; // Si false, l'objet est ramassé

    public Item(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public abstract void onCollect(Player p);

    public boolean isColliding(Player p) {
        if (!active) return false;
        return p.getX() < x + size && p.getX() + p.getSize() > x &&
               p.getY() < y + size && p.getY() + p.getSize() > y;
    }
    
    public boolean isActive() { return active; }
}