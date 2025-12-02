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
        // Change X au lieu de Y
        enemy.setX(enemy.getX() + (speed * direction));
        
        if (enemy.getX() > rightLimit) direction = -1;
        if (enemy.getX() < leftLimit) direction = 1;
    }
}