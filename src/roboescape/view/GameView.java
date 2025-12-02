package roboescape.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import roboescape.controller.GameController;
import roboescape.model.player.Player;
import roboescape.patterns.composite.Level;
import roboescape.patterns.factory.LevelFactory;

public class GameView extends StackPane {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Player player;
    private final GameController controller;
    private Level level;

    // --- GESTION DES NIVEAUX ---
    private int currentLevelIndex = 1;
    private boolean gameFinished = false;

    public GameView() {
        // Initialisation du Canvas (Zone de dessin)
        this.canvas = new Canvas(800, 600);
        this.gc = canvas.getGraphicsContext2D();

        // Création du joueur et du contrôleur
        this.player = new Player();
        this.controller = new GameController(player);

        this.getChildren().add(canvas);
        
        // CHARGER LE PREMIER NIVEAU (Level 1)
        loadLevel(currentLevelIndex);

        // Lancer la boucle de jeu
        startGameLoop();
    }

    /**
     * Charge un niveau spécifique via la Factory
     */
    private void loadLevel(int index) {
        // Appel à la Factory pour créer le niveau demandé
        Level newLevel = LevelFactory.createLevel(index);
        
        if (newLevel != null) {
            // Si le niveau existe, on le charge
            this.level = newLevel;
            controller.setLevel(level); // IMPORTANT : Lier le nouveau niveau au contrôleur pour les collisions
            player.resetPosition();     // Remettre le joueur au départ (et reset l'état de victoire)
            System.out.println("Niveau " + index + " chargé !");
        } else {
            // Si la Factory renvoie null, c'est qu'il n'y a plus de niveaux
            gameFinished = true;
            player.setWon(true); // On laisse le statut "Gagné" pour bloquer les mouvements
            System.out.println("Tous les niveaux sont terminés !");
        }
    }

    public GameController getController() {
        return controller;
    }

    private void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // 1. GESTION TRANSITION NIVEAUX
                // Si le joueur a gagné le niveau actuel et que le jeu n'est pas totalement fini
                if (player.hasWon() && !gameFinished) {
                    currentLevelIndex++; // On passe au niveau suivant
                    loadLevel(currentLevelIndex);
                    return; // On saute une frame pour charger proprement
                }

                // 2. EFFACER L'ÉCRAN (Fond Gris Foncé)
                gc.setFill(Color.rgb(30, 30, 35));
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // 3. MISE À JOUR ET DESSIN DU NIVEAU
                if (level != null) {
                    level.update();
                    level.render(gc);
                }

                // 4. MISE À JOUR ET DESSIN DU JOUEUR
                // On met à jour le joueur seulement s'il est vivant ou si le jeu est fini (pour l'affichage)
                if (player.isAlive() || gameFinished) {
                    controller.update(canvas.getWidth(), canvas.getHeight());
                    player.render(gc);
                }
                
                // 5. INTERFACE UTILISATEUR (HUD)
                drawHUD();
            }
        };
        timer.start();
    }

    private void drawHUD() {
        // Fond semi-transparent en haut à gauche pour les stats
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        gc.fillRect(10, 10, 200, 100);

        // --- AFFICHAGE DES STATS ---
        gc.setFill(Color.WHITE);
        
        // Afficher le niveau actuel en haut à droite
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gc.fillText("LEVEL " + currentLevelIndex, 700, 30);
        
        // Vitesse
        gc.setFont(Font.font("Arial", 14));
        gc.fillText("Vitesse: " + String.format("%.1f", player.getSpeed()), 20, 30);
        
        // Bouclier
        if (player.hasShield()) {
            gc.setFill(Color.CYAN);
            gc.fillText("BOUCLIER: ACTIF", 20, 50);
        } else {
            gc.setFill(Color.GRAY);
            gc.fillText("Bouclier: Non", 20, 50);
        }

        // Vie (Rouge)
        gc.setFill(Color.rgb(255, 80, 80));
        gc.fillText("Vie: " + player.getHealth(), 20, 70);

        // Score (Or)
        gc.setFill(Color.GOLD);
        gc.fillText("Score: " + player.getScore(), 20, 90);

        // --- GESTION DES ÉCRANS DE FIN (Game Over / Victoire) ---
        
        // Cas 1 : GAME OVER
        if (!player.isAlive()) {
            drawFullScreenMessage(Color.RED, "GAME OVER", "Score Final: " + player.getScore());
        } 
        // Cas 2 : VICTOIRE TOTALE (Tous les niveaux finis)
        else if (gameFinished) {
            drawFullScreenMessage(Color.LIMEGREEN, "VICTOIRE TOTALE !", "Score Final: " + player.getScore());
        }
    }

    // Méthode utilitaire pour afficher les messages plein écran
    private void drawFullScreenMessage(Color color, String title, String subtitle) {
        // Voile noir transparent sur tout l'écran
        gc.setFill(Color.rgb(0, 0, 0, 0.8));
        gc.fillRect(0, 0, 800, 600);
        
        // Titre
        gc.setFill(color);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        // Centrage approximatif
        gc.fillText(title, 200, 300);
        
        // Sous-titre
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 20));
        gc.fillText(subtitle, 320, 350);
    }
}