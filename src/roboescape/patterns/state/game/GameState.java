package roboescape.patterns.state.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public interface GameState {
    void update();
    void render(GraphicsContext gc);
    void handleInput(KeyCode code);
    void handleKeyRelease(KeyCode code);
}