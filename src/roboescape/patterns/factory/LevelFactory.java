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
import roboescape.patterns.decorator.InvincibilityBoost;
import roboescape.patterns.decorator.ShieldBoost;
import roboescape.patterns.decorator.SpeedBoost;
import roboescape.patterns.strategy.ChasePlayerStrategy;
import roboescape.patterns.strategy.HorizontalPatrolStrategy;
import roboescape.patterns.strategy.RandomMovementStrategy;
import roboescape.patterns.strategy.VerticalPatrolStrategy;
import roboescape.patterns.util.PatternLogger;

public class LevelFactory {

    private static final int TILE_SIZE = 40;
    private static final int COLS = 20; // 800px / 40
    private static final int ROWS = 15; // 600px / 40

    // Générateur de nombres aléatoires
    private static final Random random = new Random();

    public static Level createLevel(int levelNumber, roboescape.model.player.Player player) {
        PatternLogger.logFactoryCreation("LevelFactory", "Level " + levelNumber);
        Level level = new Level();

        // --- 1. MURS EXTÉRIEURS (Toujours là) ---
        level.addWall(new Wall(0, 0, 800, 20)); // Haut
        level.addWall(new Wall(0, 580, 800, 20)); // Bas
        level.addWall(new Wall(0, 0, 20, 600)); // Gauche
        level.addWall(new Wall(780, 0, 20, 600)); // Droite

        // Si c'est le niveau 1, on charge le tuto facile (carte fixe)
        if (levelNumber == 1) {
            return loadFromMap(level, LEVEL_1_MAP, player);
        }

        // SINON : GÉNÉRATION AUTOMATIQUE (Niveau 2 à Infini)
        return generateProceduralLevel(level, levelNumber, player);
    }

    // ==========================================
    // ALGORITHME DE GÉNÉRATION PROCÉDURALE
    // ==========================================
    private static Level generateProceduralLevel(Level level, int difficulty, roboescape.model.player.Player player) {
        System.out.println("Génération du niveau " + difficulty + "...");

        // 1. CALCUL DE LA DIFFICULTÉ
        // Plus le niveau est haut, plus il y a de choses
        int numEnemies = 2 + (difficulty / 2); // Ex: Niv 10 = 7 ennemis
        int numTraps = 2 + difficulty; // Ex: Niv 10 = 12 pièges
        int numCoins = 5; // Toujours 5 pièces

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
        // On cherche une position sûre pour la sortie (loin du centre)
        double[] exitPos = findSafePosition(level, 50, 50, true);
        if (exitPos != null) {
            level.setExit(new Exit(exitPos[0], exitPos[1], 50));
        } else {
            // Fallback coin haut gauche si échec
            level.setExit(new Exit(50, 50, 50));
        }

        // 4. PLACEMENT DES PIÈGES & ITEMS
        addRandomItems(level, numTraps, "TRAP");
        addRandomItems(level, numCoins, "COIN");
        addRandomItems(level, 1, "HEAL"); // 1 Soin garanti
        if (difficulty > 3)
            addRandomItems(level, 1, "POWERUP"); // Bonus dès niv 4

        // 5. PLACEMENT DES ENNEMIS (Adaptatif)
        for (int i = 0; i < numEnemies; i++) {
            // Cherche position sûre pour ennemi (30x30)
            double[] pos = findSafePosition(level, 30, 30, true);
            if (pos == null)
                continue; // Skip si pas de place

            double ex = pos[0];
            double ey = pos[1];

            Enemy enemy;
            // Choix du type d'ennemi selon la difficulté
            if (difficulty >= 6 && random.nextDouble() < 0.3) {
                // Niveau 6+ : Parfois des Traqueurs (Strategy Chase)
                enemy = new Enemy(ex, ey, new ChasePlayerStrategy(player));
            } else if (difficulty >= 5 && random.nextBoolean()) {
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
            // Taille standard des items ~20-30
            double[] pos = findSafePosition(level, 30, 30, false);
            if (pos == null)
                continue;

            double x = pos[0];
            double y = pos[1];

            switch (type) {
                case "TRAP" -> level.addItem(new TrapItem(x, y));
                case "COIN" -> level.addItem(new CoinItem(x, y));
                case "HEAL" -> level.addItem(new BatteryItem(x, y));
                case "POWERUP" -> {
                    double r = random.nextDouble();
                    if (r < 0.33)
                        level.addItem(new PowerUpItem(x, y, new SpeedBoost(), Color.ORANGE));
                    else if (r < 0.66)
                        level.addItem(new PowerUpItem(x, y, new ShieldBoost(), Color.CYAN));
                    else
                        level.addItem(new PowerUpItem(x, y, new InvincibilityBoost(), Color.GOLD));
                }
            }
        }
    }

    // Trouve une position x,y libre (pas dans un mur)
    // farFromCenter : si true, force à être loin du spawn (pour ennemis/exit)
    private static double[] findSafePosition(Level level, double w, double h, boolean farFromCenter) {
        for (int attempts = 0; attempts < 100; attempts++) {
            double x = 60 + random.nextInt(680); // Marge bords
            double y = 60 + random.nextInt(480);

            // Vérif distance centre (Spawn 400,300)
            double dist = Math.sqrt(Math.pow(x - 400, 2) + Math.pow(y - 300, 2));
            if (farFromCenter && dist < 200)
                continue; // Trop près
            if (!farFromCenter && dist < 50)
                continue; // Évite pile sur le spawn

            // Vérif Collision Murs
            if (!level.checkCollisionRect(x, y, w, h)) {
                return new double[] { x, y };
            }
        }
        return null; // Pas trouvé
    }

    // ==========================================
    // PARSER MAP (Pour le Niveau 1 Fixe)
    // ==========================================
    private static Level loadFromMap(Level level, String[] mapData, roboescape.model.player.Player player) {
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
                    case 'P' -> {
                        double r = random.nextDouble();
                        if (r < 0.5)
                            level.addItem(new PowerUpItem(x, y, new SpeedBoost(), Color.ORANGE));
                        else
                            level.addItem(new PowerUpItem(x, y, new InvincibilityBoost(), Color.GOLD));
                    }
                    case 'S' -> level.addEnemy(new Enemy(x, y, new VerticalPatrolStrategy(y - 50, y + 50)));
                    case 'H' -> level.addEnemy(new Enemy(x, y, new ChasePlayerStrategy(player)));
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