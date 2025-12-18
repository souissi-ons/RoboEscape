package roboescape.patterns.util;

import java.util.Arrays;

/**
 * Centralized logger for Design Pattern usage tracing.
 * Provides surgical logging for State, Decorator, Observer, Composite, and
 * Factory patterns.
 * 
 * Toggle ENABLED flag to control output.
 */
public class PatternLogger {

    private static final boolean ENABLED = true;
    private static final String PREFIX = "[PATTERN]";

    // ========== STATE PATTERN ==========

    /**
     * Logs state transitions (State Pattern)
     * 
     * @param from    Previous state name
     * @param to      New state name
     * @param context Object undergoing transition (e.g., "GameView", "Player")
     */
    public static void logStateTransition(String from, String to, String context) {
        log("STATE", "%s: %s → %s", context, from, to);
    }

    // ========== DECORATOR PATTERN ==========

    /**
     * Logs decorator application (Decorator Pattern)
     * 
     * @param decorator Name of the decorator being applied
     * @param target    Target object being decorated
     */
    public static void logDecoratorApplied(String decorator, String target) {
        log("DECORATOR", "%s applied to %s", decorator, target);
    }

    /**
     * Logs decorator removal/expiration (Decorator Pattern)
     * 
     * @param decorator Name of the decorator being removed
     */
    public static void logDecoratorExpired(String decorator) {
        log("DECORATOR", "%s expired", decorator);
    }

    // ========== OBSERVER PATTERN ==========

    /**
     * Logs observer notification (Observer Pattern)
     * 
     * @param observer Observer being notified
     * @param event    Event type
     * @param data     Event data
     */
    public static void logObserverNotified(String observer, String event, Object... data) {
        log("OBSERVER", "%s notified: %s %s", observer, event, formatData(data));
    }

    // ========== COMPOSITE PATTERN ==========

    /**
     * Logs composite operations (Composite Pattern)
     * 
     * @param operation  Operation being performed (e.g., "Update", "Render")
     * @param childCount Number of children affected
     */
    public static void logCompositeOperation(String operation, int childCount) {
        log("COMPOSITE", "%s on %d children", operation, childCount);
    }

    // ========== FACTORY PATTERN ==========

    /**
     * Logs factory object creation (Factory Pattern)
     * 
     * @param factory Factory name
     * @param product Product being created
     */
    public static void logFactoryCreation(String factory, String product) {
        log("FACTORY", "%s created %s", factory, product);
    }

    // ========== COMMAND PATTERN ==========

    /**
     * Logs command execution (Command Pattern)
     * 
     * @param commandName Name of the command being executed
     */
    public static void logCommandExecution(String commandName) {
        log("COMMAND", "%s executed", commandName);
    }

    // ========== INTERNAL ==========

    private static void log(String pattern, String format, Object... args) {
        if (ENABLED) {
            System.out.printf("%s [%s] %s%n", PREFIX, pattern, String.format(format, args));
        }
    }

    private static String formatData(Object[] data) {
        if (data == null || data.length == 0)
            return "";
        return Arrays.toString(data);
    }
}
