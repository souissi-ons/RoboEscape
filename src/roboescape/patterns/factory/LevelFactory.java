package roboescape.patterns.factory;

import java.util.Random;
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
import roboescape.patterns.strategy.HorizontalPatrolStrategy;
import roboescape.patterns.strategy.RandomMovementStrategy;
import roboescape.patterns.strategy.VerticalPatrolStrategy;

public class LevelFactory {

    private static final int TILE_SIZE = 40;
    private static final int COLS = 20; // 800px / 40
    private static final int ROWS = 15; // 600px / 40
    
    // Générateur de nombres aléatoires
    private static final Random random = new Random();

    public static Level createLevel(int levelNumber) {
        Level level = new Level();

        // --- 1. MURS EXTÉRIEURS (Toujours là) ---
        level.addWall(new Wall(0, 0, 800, 20));     // Haut
        level.addWall(new Wall(0, 580, 800, 20));   // Bas
        level.addWall(new Wall(0, 0, 20, 600));     // Gauche
        level.addWall(new Wall(780, 0, 20, 600));   // Droite

        // Si c'est le niveau 1, on charge le tuto facile (carte fixe)
        if (levelNumber == 1) {
            return loadFromMap(level, LEVEL_1_MAP);
        } 
        
        // SINON : GÉNÉRATION AUTOMATIQUE (Niveau 2 à Infini)
        return generateProceduralLevel(level, levelNumber);
    }

    // ==========================================
    // ALGORITHME DE GÉNÉRATION PROCÉDURALE
    // ==========================================
    private static Level generateProceduralLevel(Level level, int difficulty) {
        System.out.println("Génération du niveau " + difficulty + "...");

        // 1. CALCUL DE LA DIFFICULTÉ
        // Plus le niveau est haut, plus il y a de choses
        int numEnemies = 2 + (difficulty / 2);      // Ex: Niv 10 = 7 ennemis
        int numTraps = 2 + difficulty;              // Ex: Niv 10 = 12 pièges
        int numCoins = 5;                           // Toujours 5 pièces
        
        // Densité des murs (Chance qu'une case soit un mur)
        // Niv 2 = 10%, Niv 10 = 20%, Max 30%
        double wallDensity = Math.min(0.1 + (difficulty * 0.01), 0.30); 

        // 2. PLACEMENT DES MURS (ALÉATOIRE)
        // On parcourt la grille (on évite les bords qui sont déjà faits)
        for (int row = 1; row < ROWS - 1; row++) {
            for (int col = 1; col < COLS - 1; col++) {
                
                // ZONE SÛRE (Safe Zone) : Pas de mur au centre (Spawn 400,300)
                // Le centre est environ à col=10, row=7
                if (Math.abs(col - 10) < 3 && Math.abs(row - 7) < 3) {
                    continue; // On laisse vide pour que le joueur ne naisse pas dans un mur
                }

                // Chance de placer un mur
                if (random.nextDouble() < wallDensity) {
                    level.addWall(new Wall(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE));
                }
            }
        }

        // 3. PLACEMENT DE LA SORTIE
        // On la met toujours loin, dans un coin aléatoire
        boolean exitTop = random.nextBoolean();
        double exitX = random.nextBoolean() ? 50 : 700;
        double exitY = exitTop ? 50 : 500;
        level.setExit(new Exit(exitX, exitY, 50));

        // 4. PLACEMENT DES PIÈGES & ITEMS
        addRandomItems(level, numTraps, "TRAP");
        addRandomItems(level, numCoins, "COIN");
        addRandomItems(level, 1, "HEAL"); // 1 Soin garanti
        if (difficulty > 3) addRandomItems(level, 1, "POWERUP"); // Bonus dès niv 4

        // 5. PLACEMENT DES ENNEMIS (Adaptatif)
        for (int i = 0; i < numEnemies; i++) {
            double ex = 100 + random.nextInt(600);
            double ey = 100 + random.nextInt(400);
            
            // Ne pas spawner sur le joueur au centre
            if (Math.abs(ex - 400) < 100 && Math.abs(ey - 300) < 100) {
                ex += 200; // On décale si c'est trop près
            }

            Enemy enemy;
            // Choix du type d'ennemi selon la difficulté
            if (difficulty >= 5 && random.nextBoolean()) {
                // Niveau 5+ : Parfois des Scies
                enemy = new BladeEnemy(ex, ey, new HorizontalPatrolStrategy(ex - 50, ex + 50));
            } else if (difficulty >= 8 && random.nextBoolean()) {
                // Niveau 8+ : Parfois des Fous
                enemy = new Enemy(ex, ey, new RandomMovementStrategy());
            } else {
                // Ennemi Standard
                boolean vertical = random.nextBoolean();
                if (vertical) {
                    enemy = new Enemy(ex, ey, new VerticalPatrolStrategy(ey - 60, ey + 60));
                } else {
                    enemy = new Enemy(ex, ey, new HorizontalPatrolStrategy(ex - 60, ex + 60));
                }
            }
            level.addEnemy(enemy);
        }

        return level;
    }

    // Méthode utilitaire pour ajouter des objets au hasard dans les trous
    private static void addRandomItems(Level level, int count, String type) {
        for (int i = 0; i < count; i++) {
            // On cherche une position aléatoire (simple)
            double x = 60 + random.nextInt(680);
            double y = 60 + random.nextInt(480);

            // On évite le centre (Spawn)
            if (Math.abs(x - 400) < 50 && Math.abs(y - 300) < 50) continue;

            switch (type) {
                case "TRAP" -> level.addItem(new TrapItem(x, y));
                case "COIN" -> level.addItem(new CoinItem(x, y));
                case "HEAL" -> level.addItem(new BatteryItem(x, y));
                case "POWERUP" -> {
                    if (random.nextBoolean())
                        level.addItem(new PowerUpItem(x, y, new SpeedBoost(), Color.ORANGE));
                    else
                        level.addItem(new PowerUpItem(x, y, new ShieldBoost(), Color.CYAN));
                }
            }
        }
    }

    // ==========================================
    // PARSER MAP (Pour le Niveau 1 Fixe)
    // ==========================================
    private static Level loadFromMap(Level level, String[] mapData) {
        for (int row = 0; row < mapData.length; row++) {
            String line = mapData[row];
            for (int col = 0; col < line.length(); col++) {
                char symbol = line.charAt(col);
                double x = col * TILE_SIZE;
                double y = row * TILE_SIZE;

                switch (symbol) {
                    case '#' -> level.addWall(new Wall(x, y, TILE_SIZE, TILE_SIZE));
                    case 'E' -> level.setExit(new Exit(x, y, TILE_SIZE));
                    case 'C' -> level.addItem(new CoinItem(x + 10, y + 10));
                    case 'P' -> level.addItem(new PowerUpItem(x, y, new SpeedBoost(), Color.ORANGE));
                    case 'S' -> level.addEnemy(new Enemy(x, y, new VerticalPatrolStrategy(y - 50, y + 50)));
                }
            }
        }
        return level;
    }

    // Carte du Niveau 1 (Tutorial)
    private static final String[] LEVEL_1_MAP = {
        "####################",
        "#..................#",
        "#...C..######......#",
        "#...S..#....#..C...#",
        "#......#....#......#",
        "#......#....#......#",
        "#..#####....#####..#",
        "#..................E",
        "#..#####....#####..#",
        "#......#....#......#",
        "#...C..#....#..P...#",
        "#......######......#",
        "#..................#",
        "#..................#",
        "####################"
    };
}