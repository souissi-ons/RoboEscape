package roboescape.patterns.state.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import roboescape.view.GameView;
import roboescape.patterns.command.RestartCommand;

public class WinState implements GameState {

    private final GameView context;

    public WinState(GameView context) {
        this.context = context;
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext gc) {
        double w = 800;
        double h = 600;

        // Fond Vert Glorieux
        LinearGradient bg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(0, 40, 0)), new Stop(1, Color.rgb(0, 10, 0)));
        gc.setFill(bg);
        gc.fillRect(0, 0, w, h);

        // Texte
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.LIMEGREEN);
        gc.setFont(Font.font("Impact", 80));
        gc.fillText("MISSION ACCOMPLIE", w / 2, 250);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 30));
        gc.fillText("Score Final : " + context.getPlayer().getScore(), w / 2, 350);

        // Instructions
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            gc.setFill(Color.YELLOW);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            gc.fillText("Press R to Restart or ENTER for Menu", w / 2, 450);
        }
    }

    @Override
    public void handleInput(KeyCode code) {
        if (code == KeyCode.ENTER || code == KeyCode.ESCAPE) {
            context.restartGame();
            context.setState(new MenuState(context));
        } else if (code == KeyCode.R) {
            new RestartCommand(context).execute();
        }
    }

    @Override
    public void handleKeyRelease(KeyCode code) {
    }
}