package roboescape;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import roboescape.view.GameView;

public class RoboEscape extends Application {

    @Override
    public void start(Stage stage) {

        GameView view = new GameView();

        Scene scene = new Scene(view, 800, 600);

        scene.setOnKeyPressed(e -> view.getController().onKeyPressed(e.getCode()));
        scene.setOnKeyReleased(e -> view.getController().onKeyReleased(e.getCode()));

        stage.setScene(scene);
        stage.setTitle("RoboEscape");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
