package roboescape.patterns.command;

/**
 * Command Pattern Interface.
 * Encapsulates game actions as objects for better separation of concerns.
 */
public interface GameCommand {
    /**
     * Execute the command action.
     */
    void execute();
}
