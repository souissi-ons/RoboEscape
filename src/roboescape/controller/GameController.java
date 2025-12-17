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

    public void update(double width, double height, double deltaTime) {
        if (!player.isAlive() || player.hasWon())
            return;

        double speed = player.getSpeed();
        double distance = speed * deltaTime;

        double moveX = 0;
        double moveY = 0;

        if (left)
            moveX -= distance;
        if (right)
            moveX += distance;
        if (up)
            moveY -= distance;
        if (down)
            moveY += distance;

        if (level != null) {
            double hbSize = player.getHitboxSize(); // ex: 28px
            double offset = player.getHitboxOffset(); // ex: 6px
            double currentX = player.getX();
            double currentY = player.getY();

            // =========================================================
            // 1. GESTION AXE X (Gauche / Droite)
            // =========================================================
            if (moveX != 0) {
                double nextX = currentX + moveX;

                boolean collideX = level.checkCollisionRect(
                        nextX + offset, // X projected
                        currentY + offset, // Y current
                        hbSize, // Full Width
                        hbSize // Full Height
                );

                if (!collideX) {
                    player.move(moveX, 0);
                    currentX += moveX; // Mise à jour pour le calcul Y
                }
            }

            // =========================================================
            // 2. GESTION AXE Y (Haut / Bas)
            // =========================================================
            if (moveY != 0) {
                double nextY = currentY + moveY;

                boolean collideY = level.checkCollisionRect(
                        currentX + offset, // X current
                        nextY + offset, // Y projected
                        hbSize, // Full Width
                        hbSize // Full Height
                );

                if (!collideY) {
                    player.move(0, moveY);
                }
            }

            // --- INTERACTIONS (Items, Ennemis, Sortie) ---
            level.checkItemCollection(player);

            for (Enemy e : level.getEnemies()) {
                // Hitbox légèrement permissive pour les ennemis
                if (e.checkCollision(player.getX() + 5, player.getY() + 5, player.getSize() - 10)) {
                    if (!player.hasShield())
                        player.takeDamage(1);
                }
            }

            if (level.getExit() != null && level.getExit().checkCollision(player)) {
                player.setWon(true);
                player.addScore(2000);
            }

        } else {
            // Pas de niveau (mode test)
            player.move(moveX, moveY);
        }

        player.update(width, height);
    }

    public void onKeyPressed(KeyCode code) {
        switch (code) {
            case UP -> up = true;
            case DOWN -> down = true;
            case LEFT -> left = true;
            case RIGHT -> right = true;
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