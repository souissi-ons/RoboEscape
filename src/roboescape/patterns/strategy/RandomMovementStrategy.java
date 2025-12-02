package roboescape.patterns.strategy;

import java.util.Random;
import roboescape.model.enemy.Enemy;

public class RandomMovementStrategy implements MovementStrategy {
    private double dx = 1;
    private double dy = 1;
    private int changeTimer = 0;
    private Random random = new Random();

    @Override
    public void move(Enemy enemy, double speed) {
        changeTimer++;
        
        // Change de direction aléatoirement
        if (changeTimer > 60) {
            dx = random.nextBoolean() ? 1 : -1;
            dy = random.nextBoolean() ? 1 : -1;
            changeTimer = 0;
        }

        // Bouge
        enemy.setX(enemy.getX() + (dx * speed * 0.5)); // Plus lent
        enemy.setY(enemy.getY() + (dy * speed * 0.5));

        // Garde-fou simple pour qu'il ne sorte pas trop de l'écran
        if (enemy.getX() < 0) enemy.setX(0);
        if (enemy.getX() > 750) enemy.setX(750);
        if (enemy.getY() < 0) enemy.setY(0);
        if (enemy.getY() > 550) enemy.setY(550);
    }
}