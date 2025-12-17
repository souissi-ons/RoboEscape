package roboescape.patterns.composite;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import roboescape.model.enemy.Enemy;
import roboescape.model.items.Item;
import roboescape.model.player.Player;
import roboescape.patterns.util.PatternLogger;

public class Level implements LevelComponent {
    private List<Wall> walls = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private Exit exit;

    public void addWall(Wall w) {
        walls.add(w);
    }

    public void addItem(Item i) {
        items.add(i);
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public void setExit(Exit e) {
        this.exit = e;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (exit != null)
            exit.render(gc);
        items.forEach(i -> i.render(gc));
        enemies.forEach(e -> e.render(gc));
        walls.forEach(w -> w.render(gc));
    }

    @Override
    public void update() {
        PatternLogger.logCompositeOperation("Update", enemies.size() + items.size());
        enemies.forEach(Enemy::update);
        items.forEach(Item::update);
    }

    // COLLISION STRICTE
    public boolean checkCollisionRect(double x, double y, double w, double h) {
        for (Wall wall : walls) {
            // STRICTEMENT < et >
            if (x < wall.getX() + wall.getWidth() &&
                    x + w > wall.getX() &&
                    y < wall.getY() + wall.getHeight() &&
                    y + h > wall.getY()) {
                return true;
            }
        }
        return false;
    }

    public void checkItemCollection(Player p) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isActive() && items.get(i).isColliding(p))
                items.get(i).onCollect(p);
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Exit getExit() {
        return exit;
    }
}