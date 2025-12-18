package roboescape.patterns.strategy;

import roboescape.model.enemy.Enemy;

public class VerticalPatrolStrategy implements MovementStrategy {
    private int direction = 1;
    private double topLimit, bottomLimit;

    public VerticalPatrolStrategy(double top, double bottom) {
        this.topLimit = top;
        this.bottomLimit = bottom;
    }

    @Override
    public void move(Enemy enemy, double speed) {
        double nextY = enemy.getY() + (speed * direction);

        // 1. Check Level Collision (Walls)
        if (enemy.getLevel() != null) {
            boolean collision = enemy.getLevel().checkCollisionRect(
                    enemy.getX(), nextY, enemy.getSize(), enemy.getSize());

            if (collision) {
                direction *= -1; // Bounce
                return;
            }
        }

        // 2. Check Patrol Limits
        if (nextY > bottomLimit)
            direction = -1;
        if (nextY < topLimit)
            direction = 1;

        enemy.setY(nextY);
    }
}