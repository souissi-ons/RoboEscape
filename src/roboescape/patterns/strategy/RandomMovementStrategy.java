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
        // Bouge
        double nextX = enemy.getX() + (dx * speed * 0.5);
        double nextY = enemy.getY() + (dy * speed * 0.5);

        if (enemy.getLevel() != null) {
            boolean collision = enemy.getLevel().checkCollisionRect(
                    nextX, nextY, enemy.getSize(), enemy.getSize());
            if (collision) {
                dx *= -1;
                dy *= -1;
                changeTimer = 0; // Pick new direction sooner
                return;
            }
        }

        enemy.setX(nextX);
        enemy.setY(nextY);

        // Garde-fou simple pour qu'il ne sorte pas trop de l'écran
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