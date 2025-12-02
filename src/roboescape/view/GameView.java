package roboescape.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import roboescape.controller.GameController;
import roboescape.model.enemy.Enemy;
import roboescape.model.player.Player;
import roboescape.patterns.composite.Level;
import roboescape.patterns.composite.Wall;

public class GameView extends StackPane {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Player player;
    private final GameController controller;
    private Level level;

    public GameView() {
        this.canvas = new Canvas(800, 600);
        this.gc = canvas.getGraphicsContext2D();

        this.player = new Player();
        this.controller = new GameController(player);

        this.getChildren().add(canvas);

        // --- INITIALISATION DU NIVEAU ---
        initLevel();

        startGameLoop();
    }

    private void initLevel() {
        level = new Level();
        controller.setLevel(level);

        // 1. Cadre extérieur (Loin du centre)
        level.addWall(new Wall(0, 0, 800, 20));
        level.addWall(new Wall(0, 580, 800, 20));
        level.addWall(new Wall(0, 0, 20, 600));
        level.addWall(new Wall(780, 0, 20, 600));

        // 2. Obstacles (ON LAISSE LE CENTRE 400,300 VIDE !)
        
        // Mur à gauche
        level.addWall(new Wall(100, 100, 20, 400)); 
        
        // Mur à droite
        level.addWall(new Wall(680, 100, 20, 400));
        
        // Îlots (mais pas au centre)
        level.addWall(new Wall(250, 150, 100, 50));
        level.addWall(new Wall(250, 400, 100, 50));
        level.addWall(new Wall(450, 150, 100, 50));
        level.addWall(new Wall(450, 400, 100, 50));

        // --- Items & Ennemis ---
        level.addItem(new roboescape.model.items.CoinItem(400, 100)); // Haut
        level.addItem(new roboescape.model.items.CoinItem(400, 500)); // Bas

        // Ennemi qui tourne autour du centre
        level.addEnemy(new roboescape.model.enemy.Enemy(200, 300, 
            new roboescape.patterns.strategy.VerticalPatrolStrategy(200, 400)));

        level.setExit(new roboescape.patterns.composite.Exit(720, 300, 50));
    }

    public GameController getController() {
        return controller;
    }

    private void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // A. EFFACER / FOND
                gc.setFill(Color.rgb(30, 30, 35));
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // B. DESSINER NIVEAU (Murs + Items + Ennemis)
                level.update();
                level.render(gc);

                // C. DESSINER JOUEUR (si vivant)
                if (player.isAlive()) {
                    controller.update(canvas.getWidth(), canvas.getHeight());
                    player.render(gc);
                } else {
                    // Si mort, on dessine juste le cadavre (optionnel) ou rien
                }

                // D. INTERFACE (HUD)
                drawHUD();
            }
        };
        timer.start();
    }

    private void drawHUD() {
        // Fond semi-transparent
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        gc.fillRect(10, 10, 180, 90);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // 1. Vitesse
        gc.setFill(Color.WHITE);
        gc.fillText("Vitesse: " + String.format("%.1f", player.getSpeed()), 20, 30);

        // 2. Bouclier
        if (player.hasShield()) {
            gc.setFill(Color.CYAN);
            gc.fillText("BOUCLIER ACTIF", 20, 50);
        } else {
            gc.setFill(Color.GRAY);
            gc.fillText("Bouclier: Non", 20, 50);
        }

        // 3. Vie (Rouge)
        gc.setFill(Color.rgb(255, 80, 80));
        gc.fillText("Vie: " + player.getHealth(), 20, 70);

        // 4. Score (Jaune)
        gc.setFill(Color.GOLD);
        gc.fillText("Score: " + player.getScore(), 20, 90);

        // 5. ECRAN GAME OVER
        if (!player.isAlive()) {
            gc.setFill(Color.rgb(0, 0, 0, 0.7)); // Voile noir
            gc.fillRect(0, 0, 800, 600);

            gc.setFill(Color.RED);
            gc.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
            gc.fillText("GAME OVER", 200, 300);

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", 20));
            gc.fillText("Score Final : " + player.getScore(), 320, 350);
        } else if (player.hasWon()) { // <--- NOUVEAU
            // Fond vert semi-transparent
            gc.setFill(Color.rgb(0, 100, 0, 0.8));
            gc.fillRect(0, 0, 800, 600);

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
            gc.fillText("VICTOIRE !", 230, 300);

            gc.setFont(Font.font("Arial", 20));
            gc.fillText("Score Final : " + player.getScore(), 320, 350);
            gc.fillText("Bravo Robot !", 340, 380);
        }
    }
}
