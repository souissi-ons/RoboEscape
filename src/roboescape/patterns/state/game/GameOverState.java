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
        gc.fillText("Score Final : " + context.getPlayer().getScore(), w / 2, 350);

        // Instructions mises à jour
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            gc.setFill(Color.YELLOW);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            gc.fillText("Appuyez sur ENTRÉE pour le Menu", w / 2, 450);
        }
    }

    @Override
    public void handleInput(KeyCode code) {
        // MODIFICATION ICI : Entrée retourne au Menu
        if (code == KeyCode.ENTER || code == KeyCode.ESCAPE) {
            context.resetGame(); // Nettoie le niveau actuel
            context.setState(new MenuState(context)); // Retour au Menu
        }
    }

    @Override
    public void handleKeyRelease(KeyCode code) {
    }
}