
package roboescape;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RoboEscape extends Application {


    @Override
public void start(Stage primaryStage) {
    primaryStage.setTitle("RoboEscape");

    StackPane root = new StackPane();
        Label test = new Label("JavaFX fonctionne !");
    test.setStyle("-fx-font-size: 32px; -fx-text-fill: blue;");

    root.getChildren().add(test);

    Scene scene = new Scene(root, 900, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
}


    public static void main(String[] args) {
        launch(args);
    }
    
}
