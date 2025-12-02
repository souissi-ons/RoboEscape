package roboescape.controller;

import javafx.scene.input.KeyCode;
import roboescape.model.enemy.Enemy;
import roboescape.model.player.Player;
import roboescape.patterns.composite.Level;

public class GameController {

    private boolean up, down, left, right;
    private final Player player;
    private Level level;

    public GameController(Player p) { this.player = p; }
    public void setLevel(Level level) { this.level = level; }

    public void update(double width, double height) {
        if (!player.isAlive() || player.hasWon()) return;

        double speed = player.getSpeed();
        double moveX = 0;
        double moveY = 0;

        if (left) moveX -= speed;
        if (right) moveX += speed;
        if (up) moveY -= speed;
        if (down) moveY += speed;

        if (level != null) {
            double hbSize = player.getHitboxSize();     // 28px
            double offset = player.getHitboxOffset();   // 6px
            double currentX = player.getX();
            double currentY = player.getY();

            // Marge pour glisser sur les murs latéraux
            double slideMargin = 4.0; 
            // Marge pour ignorer l'arrière du robot (Anti-Collage)
            double ignoreBackMargin = 10.0; 

            // =========================================================
            // 1. GESTION AXE X (Gauche / Droite)
            // =========================================================
            if (moveX != 0) {
                double nextX = currentX + moveX;

                // Si je vais à DROITE : J'ignore ce qui se passe à ma GAUCHE (l'arrière)
                double ignoreLeft = (moveX > 0) ? ignoreBackMargin : 0;
                // Si je vais à GAUCHE : J'ignore ce qui se passe à ma DROITE
                double ignoreRight = (moveX < 0) ? ignoreBackMargin : 0;

                boolean collideX = level.checkCollisionRect(
                    nextX + offset + ignoreLeft,    // X ajusté
                    currentY + offset + slideMargin,// Y + 4px (Ignore frottement Haut)
                    hbSize - ignoreLeft - ignoreRight, // Largeur réduite (Focus sur l'avant)
                    hbSize - (slideMargin * 2)      // Hauteur réduite (Ignore frottement Bas)
                );

                if (!collideX) {
                    player.move(moveX, 0);
                    currentX += moveX;
                }
            }

            // =========================================================
            // 2. GESTION AXE Y (Haut / Bas)
            // =========================================================
            if (moveY != 0) {
                double nextY = currentY + moveY;

                // Si je vais en BAS : J'ignore ce qui se passe en HAUT (ma tête)
                double ignoreTop = (moveY > 0) ? ignoreBackMargin : 0;
                // Si je vais en HAUT : J'ignore ce qui se passe en BAS (mes pieds)
                double ignoreBottom = (moveY < 0) ? ignoreBackMargin : 0;

                boolean collideY = level.checkCollisionRect(
                    currentX + offset + slideMargin, // X + 4px (Ignore frottement Gauche)
                    nextY + offset + ignoreTop,      // Y ajusté
                    hbSize - (slideMargin * 2),      // Largeur réduite (Ignore frottement Droite)
                    hbSize - ignoreTop - ignoreBottom // Hauteur réduite (Focus sur l'avant)
                );

                if (!collideY) {
                    player.move(0, moveY);
                }
            }

            // --- RESTE DU CODE ---
            level.checkItemCollection(player);
            for (Enemy e : level.getEnemies()) {
                if (e.checkCollision(player.getX()+5, player.getY()+5, player.getSize()-10)) {
                    if (!player.hasShield()) player.takeDamage(1);
                }
            }
            if (level.getExit() != null && level.getExit().checkCollision(player)) {
                player.setWon(true);
                player.addScore(2000);
            }
        } else {
            player.move(moveX, moveY);
        }
        player.update(width, height);
    }

    public void onKeyPressed(KeyCode code) {
        switch (code) { case UP->up=true; case DOWN->down=true; case LEFT->left=true; case RIGHT->right=true; }
    }
    public void onKeyReleased(KeyCode code) {
        switch (code) { case UP->up=false; case DOWN->down=false; case LEFT->left=false; case RIGHT->right=false; }
    }
}