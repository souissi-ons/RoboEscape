package roboescape.patterns.strategy;

import roboescape.model.enemy.Enemy;

public class HorizontalPatrolStrategy implements MovementStrategy {
    private int direction = 1;
    private double leftLimit, rightLimit;

    public HorizontalPatrolStrategy(double left, double right) {
        this.leftLimit = left;
        this.rightLimit = right;
    }

    @Override
    public void move(Enemy enemy, double speed) {
        double nextX = enemy.getX() + (speed * direction);

        // 1. Check Level Collision (Walls)
        if (enemy.getLevel() != null) {
            boolean collision = enemy.getLevel().checkCollisionRect(
                    nextX, enemy.getY(), enemy.getSize(), enemy.getSize());

            if (collision) {
                direction *= -1; // Bounce
                return; // Abort move
            }
        }

        // 2. Check Patrol Limits
        if (nextX > rightLimit)
            direction = -1;
        if (nextX < leftLimit)
            direction = 1;

        enemy.setX(nextX);
    }
}