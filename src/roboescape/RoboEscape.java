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

        // Correction : On appelle onKeyPressed sur la VUE, pas directement sur le contrôleur
        // C'est la Vue qui décidera (via le State Pattern) quoi faire de la touche.
        scene.setOnKeyPressed(e -> view.onKeyPressed(e.getCode()));
        scene.setOnKeyReleased(e -> view.onKeyReleased(e.getCode()));

        stage.setScene(scene);
        stage.setTitle("RoboEscape - Pattern Edition");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}