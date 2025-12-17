package roboescape.patterns.state.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import roboescape.view.GameView;

public class PauseState implements GameState {

    private final GameView context;

    public PauseState(GameView context) {
        this.context = context;
    }

    @Override
    public void update(double deltaTime) {
        // Le jeu est figé
    }

    @Override
    public void render(GraphicsContext gc) {
        double w = 800;
        double h = 600;

        // 1. DESSINER LE JEU EN FOND (Figé)
        context.getPlayingState().render(gc);

        // 2. VOILE SOMBRE (Plus foncé pour bien détacher le texte)
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, w, h);

        // 3. TITRE "SYSTÈME EN PAUSE"
        gc.setTextAlign(TextAlignment.CENTER);

        // Ombre du texte
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Impact", 80));
        gc.fillText("PAUSE", w / 2 + 4, 254);

        // Texte Principal (Cyan Néon)
        gc.setFill(Color.CYAN);
        gc.fillText("PAUSE", w / 2, 250);

        // Ligne de décoration
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(w / 2 - 100, 270, w / 2 + 100, 270);

        // 4. INSTRUCTIONS

        // Option 1 : Reprendre (Clignote doucement)
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            gc.setFill(Color.YELLOW);
        } else {
            gc.setFill(Color.WHITE);
        }
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        gc.fillText("Appuyez sur [ESPACE] pour Reprendre", w / 2, 350);

        // Option 2 : Menu
        gc.setFill(Color.LIGHTGRAY);
        gc.setFont(Font.font("Arial", 18));
        gc.fillText("Appuyez sur [M] pour retourner au Menu", w / 2, 400);

        // Petit détail RP (Role Play)
        gc.setFill(Color.rgb(0, 255, 0));
        gc.setFont(Font.font("Courier New", 12));
        gc.fillText("SYSTEM STATUS: STANDBY...", w / 2, 550);
    }

    @Override
    public void handleInput(KeyCode code) {
        if (code == KeyCode.SPACE) {
            context.setState(context.getPlayingState()); // Retour au jeu
        } else if (code == KeyCode.M) {
            context.setState(new MenuState(context)); // Retour au menu
            context.resetGame(); // Réinitialiser le jeu
        }
    }

    @Override
    public void handleKeyRelease(KeyCode code) {
    }
}