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
        enemy.setY(enemy.getY() + (speed * direction));
        
        if (enemy.getY() > bottomLimit) direction = -1;
        if (enemy.getY() < topLimit) direction = 1;
    }
}