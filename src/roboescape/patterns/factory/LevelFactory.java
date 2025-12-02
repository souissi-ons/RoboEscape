package roboescape.patterns.factory;

import javafx.scene.paint.Color;
import roboescape.model.enemy.BladeEnemy;
import roboescape.model.enemy.Enemy;
import roboescape.model.items.BatteryItem;
import roboescape.model.items.CoinItem;
import roboescape.model.items.PowerUpItem;
import roboescape.model.items.TrapItem;
import roboescape.patterns.composite.Exit;
import roboescape.patterns.composite.Level;
import roboescape.patterns.composite.Wall;
import roboescape.patterns.decorator.ShieldBoost;
import roboescape.patterns.decorator.SpeedBoost;
import roboescape.patterns.strategy.RandomMovementStrategy;
import roboescape.patterns.strategy.VerticalPatrolStrategy;

public class LevelFactory {

    public static Level createLevel(int levelNumber) {
        Level level = new Level();

        // --- MURS EXTÉRIEURS (Communs à tous les niveaux) ---
        level.addWall(new Wall(0, 0, 800, 20));     // Haut
        level.addWall(new Wall(0, 580, 800, 20));   // Bas
        level.addWall(new Wall(0, 0, 20, 600));     // Gauche
        level.addWall(new Wall(780, 0, 20, 600));   // Droite

        switch (levelNumber) {
            case 1:
                buildLevel1(level);
                break;
            case 2:
                buildLevel2(level);
                break;
            case 3:
                buildLevel3(level);
                break;
            default:
                return null; // Pas de niveau suivant (Fin du jeu)
        }
        return level;
    }

    // ==========================================
    // NIVEAU 1 : Introduction (Celui qu'on vient de faire)
    // ==========================================
    private static void buildLevel1(Level level) {
        // Murs laissant le centre (400,300) vide
        level.addWall(new Wall(100, 100, 20, 400)); 
        level.addWall(new Wall(680, 100, 20, 400));
        level.addWall(new Wall(250, 150, 100, 50));
        level.addWall(new Wall(450, 400, 100, 50));

        // Items Faciles
        level.addItem(new CoinItem(400, 100));
        level.addItem(new CoinItem(400, 500));
        level.addItem(new PowerUpItem(200, 300, new SpeedBoost(), Color.ORANGE));

        // Ennemi simple
        level.addEnemy(new Enemy(200, 300, new VerticalPatrolStrategy(200, 400)));

        // Sortie Facile (Droite)
        level.setExit(new Exit(720, 300, 50));
    }

    // ==========================================
    // NIVEAU 2 : Le Couloir de la Mort (Scies)
    // ==========================================
    private static void buildLevel2(Level level) {
        // Architecture en "S"
        level.addWall(new Wall(150, 0, 20, 450));   // Mur 1
        level.addWall(new Wall(350, 150, 20, 450)); // Mur 2
        level.addWall(new Wall(550, 0, 20, 450));   // Mur 3

        // Ennemis Scies (Blades)
        level.addEnemy(new BladeEnemy(250, 100, new VerticalPatrolStrategy(50, 500)));
        level.addEnemy(new BladeEnemy(450, 500, new VerticalPatrolStrategy(50, 500)));
        level.addEnemy(new BladeEnemy(650, 100, new VerticalPatrolStrategy(50, 500)));

        // Beaucoup de pièces risquées
        for(int i=0; i<5; i++) level.addItem(new CoinItem(250, 100 + i*80));
        for(int i=0; i<5; i++) level.addItem(new CoinItem(450, 100 + i*80));

        // Un bouclier caché pour survivre
        level.addItem(new PowerUpItem(50, 50, new ShieldBoost(), Color.CYAN));

        // Sortie en bas à gauche (loin)
        level.setExit(new Exit(50, 500, 50));
    }

   // ==========================================
    // NIVEAU 3 : Le Chaos (CORRIGÉ)
    // ==========================================
    private static void buildLevel3(Level level) {
        // Des îlots partout (Attention à ne pas bloquer le centre 400,300)
        for(int i=0; i<5; i++) {
            for(int j=0; j<4; j++) {
                // On évite de placer un mur au centre (spawn)
                double wallX = 100 + i*120;
                double wallY = 100 + j*100;
                
                // Si le mur est trop proche du centre (400,300), on ne le met pas
                if (Math.abs(wallX - 400) > 60 || Math.abs(wallY - 300) > 60) {
                     if((i+j)%2 == 0) level.addWall(new Wall(wallX, wallY, 50, 50));
                }
            }
        }

        // Ennemis aléatoires
        level.addEnemy(new Enemy(100, 100, new RandomMovementStrategy()));
        level.addEnemy(new Enemy(600, 500, new RandomMovementStrategy()));
        level.addEnemy(new Enemy(600, 100, new RandomMovementStrategy()));

        // Champs de mines (DÉCALÉS pour ne pas tuer le joueur au spawn)
        level.addItem(new TrapItem(250, 300)); // Décalé à gauche
        level.addItem(new TrapItem(550, 300)); // Décalé à droite
        // level.addItem(new TrapItem(400, 300)); <--- C'était ça le coupable ! (SUPPRIMÉ)

        // Batterie de soin nécessaire (Au spawn pour aider !)
        level.addItem(new BatteryItem(400, 350));

        // Sortie au milieu des mines
        level.setExit(new Exit(350, 50, 40)); // Sortie en haut
    }
}