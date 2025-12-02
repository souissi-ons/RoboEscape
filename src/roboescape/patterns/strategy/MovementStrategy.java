package roboescape.patterns.strategy;

import roboescape.model.enemy.Enemy;

public interface MovementStrategy {
    void move(Enemy enemy, double speed);
}