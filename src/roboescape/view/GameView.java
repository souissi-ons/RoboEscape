package roboescape.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import roboescape.controller.GameController;
import roboescape.model.player.Player;
import roboescape.patterns.composite.Level;
import roboescape.patterns.composite.Room;

public class GameView extends StackPane {

    private final Canvas canvas;
    private final GraphicsContext gc;

    private final Player player;
    private final GameController controller;
    
    private Level level;


    public GameView() {

        this.canvas = new Canvas(800, 600);
        this.gc = canvas.getGraphicsContext2D();

        this.player = new Player();
        this.controller = new GameController(player);

        this.getChildren().add(canvas);
        level = new Level();
        level.add(new Room(50, 50, 300, 200));
        level.add(new Room(450, 120, 250, 200));


        startGameLoop();
    }

    private void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                level.update();
                controller.update(canvas.getWidth(), canvas.getHeight());
                level.render(gc);
                player.render(gc);
            }
        };
        timer.start();
    }

    public GameController getController() {
        return controller;
    }
}
