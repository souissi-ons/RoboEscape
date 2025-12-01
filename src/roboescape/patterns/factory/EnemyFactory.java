package roboescape.patterns.factory;

import roboescape.model.enemy.BasicEnemy;
import roboescape.model.enemy.Enemy;

public class EnemyFactory {

    public static Enemy createBasicEnemy() {
        return new BasicEnemy();
    }
}
