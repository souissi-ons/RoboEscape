package roboescape.patterns.state.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public interface GameState {
    void update(double deltaTime);

    void render(GraphicsContext gc);

    void handleInput(KeyCode code);

    void handleKeyRelease(KeyCode code);
}