package roboescape.controller;

import javafx.scene.input.KeyCode;
import roboescape.model.enemy.Enemy;
import roboescape.model.player.Player;
import roboescape.patterns.composite.Level;

public class GameController {

    private boolean up, down, left, right;
    private final Player player;
    private Level level;

    public GameController(Player p) {
        this.player = p;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void update(double width, double height) {
        if (!player.isAlive() || player.hasWon()) return;

        double speed = player.getSpeed();
        double dx = 0;
        double dy = 0;

        // Calcul de la direction voulue
        if (up) dy -= speed;
        if (down) dy += speed;
        if (left) dx -= speed;
        if (right) dx += speed;

        if (level != null) {
            // Récupération des infos de collision optimisées du Player
            double hbSize = player.getHitboxSize();     // ex: 30px
            double offset = player.getHitboxOffset();   // ex: 5px
            
            double currentX = player.getX();
            double currentY = player.getY();

            // --- TEST AXE X (Horizontal) ---
            if (dx != 0) {
                // On prédit la FUTURE position X
                double nextX = currentX + dx;
                
                // On vérifie si le rectangle de collision à cette future place touche un mur
                // Note: On utilise currentY + offset car on ne veut tester que le mouvement latéral
                boolean willCollideX = level.checkCollision(
                    nextX + offset, 
                    currentY + offset, 
                    hbSize
                );

                // LOGIQUE STRICTE : Si pas de collision, on applique. Sinon, on ne fait RIEN.
                if (!willCollideX) {
                    player.move(dx, 0);
                }
            }

            // --- TEST AXE Y (Vertical) ---
            // On réactualise X car il a peut-être changé ci-dessus
            if (dy != 0) {
                double nextY = currentY + dy;
                
                // On vérifie si le rectangle de collision à cette future place touche un mur
                boolean willCollideY = level.checkCollision(
                    player.getX() + offset, // On utilise le X actuel (potentiellement mis à jour)
                    nextY + offset, 
                    hbSize
                );

                // LOGIQUE STRICTE : Si pas de collision, on applique.
                if (!willCollideY) {
                    player.move(0, dy);
                }
            }

            // --- AUTRES INTERACTIONS (Items, Ennemis) ---
            // Ici on utilise la taille normale pour que ce soit facile de ramasser
            level.checkItemCollection(player);

            for (Enemy e : level.getEnemies()) {
                if (e.checkCollision(player.getX(), player.getY(), player.getSize())) {
                    if (!player.hasShield()) player.takeDamage(1);
                }
            }
            
            if (level.getExit() != null && level.getExit().checkCollision(player)) {
                player.setWon(true);
                player.addScore(2000);
            }

        } else {
            // Si pas de niveau, on bouge librement
            player.move(dx, dy);
        }

        player.update(width, height);
    }

    public void onKeyPressed(KeyCode code) {
        switch (code) {
            case UP -> up = true;
            case DOWN -> down = true;
            case LEFT -> left = true;
            case RIGHT -> right = true;
            case SPACE -> System.out.println("Action!");
        }
    }

    public void onKeyReleased(KeyCode code) {
        switch (code) {
            case UP -> up = false;
            case DOWN -> down = false;
            case LEFT -> left = false;
            case RIGHT -> right = false;
        }
    }
}