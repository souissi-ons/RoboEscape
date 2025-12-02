package roboescape.patterns.composite;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import roboescape.model.enemy.Enemy;
import roboescape.model.items.Item;
import roboescape.model.player.Player;

public class Level implements LevelComponent {

    private List<Wall> walls = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private Exit exit;

    public void addWall(Wall w) { walls.add(w); }
    public void addItem(Item i) { items.add(i); }
    public void addEnemy(Enemy e) { enemies.add(e); }
    public void setExit(Exit e) { this.exit = e; }

    @Override
    public void render(GraphicsContext gc) {
        walls.forEach(w -> w.render(gc));
        items.forEach(i -> i.render(gc));
        enemies.forEach(e -> e.render(gc));
        if (exit != null) exit.render(gc);
    }

    @Override
    public void update() {
        enemies.forEach(Enemy::update);
        items.forEach(Item::update);
    }

    // --- C'EST ICI QUE TOUT SE JOUE ---
    public boolean checkCollision(double x, double y, double size) {
        // On teste si le rectangle (x,y,size) touche un mur
        for (Wall w : walls) {
            if (w.intersects(x, y, size)) {
                return true; // Collision détectée !
            }
        }
        return false;
    }

    public void checkItemCollection(Player p) {
        for (Item item : items) {
            // Pour les items, on est gentil, on utilise la grosse taille du joueur
            if (item.isColliding(p)) {
                item.onCollect(p);
            }
        }
    }
    
    public List<Enemy> getEnemies() { return enemies; }
    public Exit getExit() { return exit; }
}