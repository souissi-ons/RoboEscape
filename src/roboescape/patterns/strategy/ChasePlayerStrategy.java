package roboescape.patterns.strategy;

import roboescape.model.enemy.Enemy;
import roboescape.model.player.Player;
import roboescape.patterns.util.PatternLogger;

/**
 * ChasePlayerStrategy (Strategy Pattern)
 * 
 * Advanced enemy AI that chases the player using simple pathfinding.
 * The enemy will move toward the player's position while avoiding walls.
 * This creates more challenging and dynamic gameplay.
 */
public class ChasePlayerStrategy implements MovementStrategy {

    private final Player player;
    private int logCounter = 0; // To avoid spamming logs every frame

    /**
     * Constructor
     * 
     * @param player Reference to the player to chase
     */
    public ChasePlayerStrategy(Player player) {
        this.player = player;
    }

    @Override
    public void move(Enemy enemy, double speed) {
        if (player == null || !player.isAlive()) {
            // If no player or player is dead, don't move
            return;
        }

        // Log strategy execution occasionally (every 60 frames = ~1 second)
        if (logCounter++ % 60 == 0) {
            PatternLogger.logStrategyExecution("ChasePlayerStrategy", "Enemy@" + enemy.hashCode());
        }

        // Calculate direction toward player
        double playerCenterX = player.getX() + player.getSize() / 2;
        double playerCenterY = player.getY() + player.getSize() / 2;
        double enemyCenterX = enemy.getX() + enemy.getSize() / 2;
        double enemyCenterY = enemy.getY() + enemy.getSize() / 2;

        // Calculate distance
        double deltaX = playerCenterX - enemyCenterX;
        double deltaY = playerCenterY - enemyCenterY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Normalize direction (avoid division by zero)
        if (distance < 1) {
            return; // Already at player position
        }

        double dirX = deltaX / distance;
        double dirY = deltaY / distance;

        // Calculate next position
        double moveSpeed = speed * 0.6; // Slightly slower than player for balance
        double nextX = enemy.getX() + (dirX * moveSpeed);
        double nextY = enemy.getY() + (dirY * moveSpeed);

        // Check collision with walls
        if (enemy.getLevel() != null) {
            // Try moving on X axis first
            boolean collisionX = enemy.getLevel().checkCollisionRect(
                    nextX, enemy.getY(), enemy.getSize(), enemy.getSize());

            if (!collisionX) {
                enemy.setX(nextX);
            }

            // Then try moving on Y axis
            boolean collisionY = enemy.getLevel().checkCollisionRect(
                    enemy.getX(), nextY, enemy.getSize(), enemy.getSize());

            if (!collisionY) {
                enemy.setY(nextY);
            }

            // If both blocked, try diagonal movement
            if (collisionX && collisionY) {
                boolean collisionDiag = enemy.getLevel().checkCollisionRect(
                        nextX, nextY, enemy.getSize(), enemy.getSize());
                if (!collisionDiag) {
                    enemy.setX(nextX);
                    enemy.setY(nextY);
                }
            }
        } else {
            // No level collision checking, just move
            enemy.setX(nextX);
            enemy.setY(nextY);
        }

        // Keep enemy within screen bounds
        if (enemy.getX() < 0)
            enemy.setX(0);
        if (enemy.getX() > 750)
            enemy.setX(750);
        if (enemy.getY() < 0)
            enemy.setY(0);
        if (enemy.getY() > 550)
            enemy.setY(550);
    }
}
