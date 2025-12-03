package roboescape.patterns.state.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import roboescape.view.GameView;

public class GameOverState implements GameState {

    private GameView context;

    public GameOverState(GameView context) {
        this.context = context;
    }

    @Override
    public void update() {}

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 800, 600);

        gc.setFill(Color.RED);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        gc.fillText("GAME OVER", 250, 250);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Score Final : " + context.getPlayer().getScore(), 320, 320);
        gc.fillText("Appuyez sur [ENTRÉE] pour recommencer", 220, 400);
    }

    @Override
    public void handleInput(KeyCode code) {
        if (code == KeyCode.ENTER) {
            context.resetGame();
            context.setState(context.getPlayingState());
        } else if (code == KeyCode.ESCAPE) {
            context.setState(new MenuState(context));
        }
    }

    @Override
    public void handleKeyRelease(KeyCode code) {}
}