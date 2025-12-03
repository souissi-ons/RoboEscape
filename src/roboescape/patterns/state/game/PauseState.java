package roboescape.patterns.state.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import roboescape.view.GameView;

public class PauseState implements GameState {

    private GameView context;

    public PauseState(GameView context) {
        this.context = context;
    }

    @Override
    public void update() {
        // Le jeu est figé, pas d'update physique
    }

    @Override
    public void render(GraphicsContext gc) {
        // On dessine d'abord le jeu en arrière-plan (figé)
        context.getPlayingState().render(gc);

        // Voile sombre
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        gc.fillRect(0, 0, 800, 600);

        // Texte Pause
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        gc.fillText("[ESPACE] Reprendre  -  [M] Menu", 250, 350); // Mettez à jour le texte
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("[ECHAP] Reprendre  -  [M] Menu", 250, 350);
    }

    @Override
    public void handleInput(KeyCode code) {
        if (code == KeyCode.SPACE) {
            context.setState(context.getPlayingState()); // Retour au jeu
        } else if (code == KeyCode.M) {
            context.setState(new MenuState(context)); // Retour au menu
            context.resetGame(); // Réinitialiser le jeu si on retourne au menu
        }
    }

    @Override
    public void handleKeyRelease(KeyCode code) {}
}