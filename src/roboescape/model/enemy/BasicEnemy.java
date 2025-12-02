package roboescape.model.enemy;

import roboescape.patterns.strategy.MovementStrategy;
// Si vous n'avez pas de stratégie par défaut, on peut en passer une via le constructeur
// ou en créer une "null" ou "statique" si besoin.

public class BasicEnemy extends Enemy {

    // Constructeur qui transfère les données à la classe mère
    public BasicEnemy(double x, double y, MovementStrategy strategy) {
        super(x, y, strategy);
    }

    @Override
    public void render(javafx.scene.canvas.GraphicsContext gc) {
        super.render(gc); // Utilise le rendu rouge par défaut de Enemy
        // Ou vous pouvez personnaliser le dessin ici
    }
}