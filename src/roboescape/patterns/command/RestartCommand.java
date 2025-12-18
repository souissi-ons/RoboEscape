package roboescape.patterns.command;

import roboescape.view.GameView;
import roboescape.patterns.util.PatternLogger;

/**
 * Concrete Command for restarting the game.
 * Demonstrates the Command Pattern - encapsulates restart action as an object.
 */
public class RestartCommand implements GameCommand {

    private final GameView gameView;

    public RestartCommand(GameView view) {
        this.gameView = view;
    }

    @Override
    public void execute() {
        PatternLogger.logCommandExecution("RestartCommand");
        gameView.restartGame();
    }
}
