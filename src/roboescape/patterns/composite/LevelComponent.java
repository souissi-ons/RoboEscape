package roboescape.patterns.composite;

import javafx.scene.canvas.GraphicsContext;

public interface LevelComponent {
    void render(GraphicsContext gc);
    void update();
}
