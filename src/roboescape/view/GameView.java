package roboescape.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import roboescape.controller.GameController;
import roboescape.model.player.Player;

public class GameView extends StackPane {

    private final Canvas canvas;
    private final GraphicsContext gc;

    private final Player player;
    private final GameController controller;

    public GameView() {

        this.canvas = new Canvas(800, 600);
        this.gc = canvas.getGraphicsContext2D();

        this.player = new Player();
        this.controller = new GameController(player);

        this.getChildren().add(canvas);

        startGameLoop();
    }

    private void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                controller.update(canvas.getWidth(), canvas.getHeight());
                player.render(gc);
            }
        };
        timer.start();
    }

    public GameController getController() {
        return controller;
    }
}
