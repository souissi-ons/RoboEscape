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

public class GameOverState implements GameState {

    private final GameView context;

    public GameOverState(GameView context) {
        this.context = context;
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(GraphicsContext gc) {
        double w = 800;
        double h = 600;

        // Fond Rouge
        LinearGradient bg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(40, 0, 0)), new Stop(1, Color.rgb(10, 0, 0)));
        gc.setFill(bg);
        gc.fillRect(0, 0, w, h);

        // Texte
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.RED);
        gc.setFont(Font.font("Impact", 80));
        gc.fillText("GAME OVER", w / 2, 250);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 30));
        int score = context.getPlayer().getScore();
        boolean isNewRecord = roboescape.model.HighScoreManager.getInstance().checkAndSetHighScore(score);

        gc.fillText("Score Final : " + score, w / 2, 320);

        if (isNewRecord) {
            gc.setFill(Color.GOLD);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            gc.fillText("NEW HIGH SCORE!", w / 2, 380);
        } else {
            gc.setFill(Color.GRAY);
            gc.setFont(Font.font("Arial", 20));
            gc.fillText("Best: " + roboescape.model.HighScoreManager.getInstance().getHighScore(), w / 2, 380);
        }

        // Instructions mises à jour
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