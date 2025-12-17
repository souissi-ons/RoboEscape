package roboescape.patterns.state.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import roboescape.view.GameView;

public class PlayingState implements GameState {

    private GameView context;

    public PlayingState(GameView context) {
        this.context = context;
    }

    @Override
    public void update(double deltaTime) {
        // Si le joueur gagne le niveau
        if (context.getPlayer().hasWon()) {
            context.loadNextLevel();
            return;
        }

        // Si le joueur meurt
        if (!context.getPlayer().isAlive()) {
            context.setState(new GameOverState(context));
            return;
        }

        // Mise à jour normale (Physique, Ennemis, etc.)
        if (context.getLevel() != null) {
            context.getLevel().update();
        }
        context.getController().update(800, 600, deltaTime);
    }

    @Override
    public void render(GraphicsContext gc) {
        // Fond
        gc.setFill(Color.rgb(30, 30, 35));
        gc.fillRect(0, 0, 800, 600);

        // Niveau
        if (context.getLevel() != null) {
            context.getLevel().render(gc);
        }

        // Joueur
        context.getPlayer().render(gc);

        // HUD (Délégué à la vue ou dessiné ici)
        context.drawHUD(gc);
    }

    @Override
    public void handleInput(KeyCode code) {
        if (code == KeyCode.SPACE) {
            context.setState(new PauseState(context)); // Transition vers Pause
        }
        // Si vous aviez une action sur ESPACE avant, déplacez-la sur 'E' ou 'ENTER'
        else if (code == KeyCode.E) {
            System.out.println("Action !");
        } else {
            context.getController().onKeyPressed(code);
        }
    }

    @Override
    public void handleKeyRelease(KeyCode code) {
        context.getController().onKeyReleased(code);
    }
}