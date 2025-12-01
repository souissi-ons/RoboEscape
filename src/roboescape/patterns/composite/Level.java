package roboescape.patterns.composite;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;

public class Level implements LevelComponent {

    private List<LevelComponent> children = new ArrayList<>();

    public void add(LevelComponent c) {
        children.add(c);
    }

    public void remove(LevelComponent c) {
        children.remove(c);
    }

    @Override
    public void render(GraphicsContext gc) {
        children.forEach(c -> c.render(gc));
    }

    @Override
    public void update() {
        children.forEach(LevelComponent::update);
    }
}
