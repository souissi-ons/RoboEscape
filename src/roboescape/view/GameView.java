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
import roboescape.patterns.observer.GameObserver;
import roboescape.patterns.state.game.GameState;
import roboescape.patterns.state.game.MenuState;
import roboescape.patterns.state.game.PlayingState;
import roboescape.patterns.util.PatternLogger;

public class GameView extends StackPane implements GameObserver {

    private final Canvas canvas;
    private final GraphicsContext gc;

    // Modèles
    private final Player player;
    private final GameController controller;
    private Level level;

    // Gestion des États (State Pattern)
    private GameState currentState;
    private PlayingState playingState; // On garde une référence pour reprendre la pause

    // Gestion Niveaux
    private int currentLevelIndex = 1;
    private int displayedScore = 0;
    private int displayedHealth = 3;

    public GameView() {
        this.canvas = new Canvas(800, 600);
        this.gc = canvas.getGraphicsContext2D();

        this.player = new Player();
        this.player.addObserver(this); // Observer Pattern : On écoute le joueur

        this.controller = new GameController(player);
        this.getChildren().add(canvas);

        // Initialisation des états
        this.playingState = new PlayingState(this);
        this.currentState = new MenuState(this); // On commence par le MENU

        startGameLoop();
    }

    // --- GAME LOOP ---
    private void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                // Calcul du deltaTime en secondes
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                // Sécurité : Cap du deltaTime pour éviter de gros sauts si le jeu lag
                if (deltaTime > 0.1)
                    deltaTime = 0.1;

                // Tout est délégué à l'état courant !
                currentState.update(deltaTime);
                currentState.render(gc);
            }
        };
        timer.start();
    }

    // --- GESTION DES ÉTATS ---
    public void setState(GameState state) {
        String oldState = (currentState != null) ? currentState.getClass().getSimpleName() : "null";
        String newState = state.getClass().getSimpleName();
        PatternLogger.logStateTransition(oldState, newState, "GameView");
        this.currentState = state;
    }

    public PlayingState getPlayingState() {
        return playingState;
    }

    // --- LOGIQUE DE JEU GLOBALE ---
    public void loadNextLevel() {
        currentLevelIndex++;
        Level newLevel = LevelFactory.createLevel(currentLevelIndex);
        if (newLevel != null) {
            this.level = newLevel;
            controller.setLevel(level);
            player.resetPosition();
        } else {
            // Victoire totale (Fin du jeu)
            // Vous pourriez créer un WinState ici !
            System.out.println("Victoire Totale !");
            this.setState(new MenuState(this)); // Retour menu pour l'instant
            restartGame();
        }
    }

    public void drawHUD(GraphicsContext gc) {
        gc.save();

        // --- CONFIGURATION ---
        double startX = 15;
        double startY = 15;
        double cardW = 200;
        double cardH = 95;
        double x = startX;
        double y = startY;

        // --- 1. GLASSY BACKGROUND (Compact & Transparent) ---
        // Fond très transparent (Glass)
        gc.setFill(Color.rgb(10, 20, 30, 0.3));
        gc.fillRoundRect(x, y, cardW, cardH, 10, 10);

        // Bordure technologique subtile
        gc.setStroke(Color.rgb(100, 200, 255, 0.4));
        gc.setLineWidth(1.0);
        gc.strokeRoundRect(x, y, cardW, cardH, 10, 10);

        // Ligne déco interne
        gc.setStroke(Color.rgb(255, 255, 255, 0.15));
        gc.strokeLine(x + 10, y + 28, x + cardW - 10, y + 28);

        // --- 2. STATS PRINCIPALES ---

        // SCORE (Haut Droite de la carte)
        gc.setTextAlign(javafx.scene.text.TextAlignment.RIGHT);
        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gc.fillText(String.valueOf(displayedScore), x + cardW - 15, y + 22);

        // LABEL SCORE (Petit)
        gc.setFill(Color.rgb(200, 200, 200, 0.7));
        gc.setFont(Font.font("Arial", 8));
        gc.fillText("SCORE", x + cardW - 15, y + 10);

        // LEVEL (Haut Gauche)
        gc.setTextAlign(javafx.scene.text.TextAlignment.LEFT);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.fillText("LVL " + currentLevelIndex, x + 15, y + 22);

        // --- 3. DYNAMIC HEALTH (Variable Lives) ---
        // Dessiner des carrés pour la vie. Si > 10, on change de stratégie (mais ici on
        // suppose < 20)
        double hpY = y + 45;
        double hpX = x + 15;
        double hpBlockW = 8;
        double hpBlockH = 12;
        double hpGap = 3;

        gc.setFill(Color.rgb(200, 200, 200, 0.8));
        gc.setFont(Font.font("Arial", 10));
        gc.fillText("HP", hpX, hpY - 5);

        for (int i = 0; i < displayedHealth; i++) {
            // Couleur dynamique (Rouge -> Vert)
            if (displayedHealth <= 2)
                gc.setFill(Color.rgb(255, 60, 60, 0.9));
            else
                gc.setFill(Color.rgb(60, 255, 100, 0.9));

            // Gestion retour à la ligne si trop de vies (> 15 par ligne approx)
            double offsetX = (hpBlockW + hpGap) * (i % 15);
            double offsetY = (i / 15) * (hpBlockH + hpGap);

            gc.fillRect(hpX + 20 + offsetX, hpY - 14 + offsetY, hpBlockW, hpBlockH);
        }

        // --- 4. SPEED & SHIELD ---
        double bottomY = y + 75;

        // SHIELD (Enhanced with animation)
        if (player.hasShield()) {
            // Animated pulsing effect for shield
            double pulse = Math.sin(System.currentTimeMillis() / 200.0) * 0.3 + 0.7;
            gc.setFill(Color.rgb(0, 255, 255, pulse));
            gc.fillOval(x + 15, bottomY - 6, 8, 8);

            // Outer glow ring
            gc.setStroke(Color.rgb(0, 255, 255, pulse * 0.5));
            gc.setLineWidth(2);
            gc.strokeOval(x + 13, bottomY - 8, 12, 12);

            gc.setFill(Color.CYAN);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            gc.fillText("SHIELD", x + 28, bottomY + 2);
        } else {
            gc.setStroke(Color.GRAY);
            gc.strokeOval(x + 15, bottomY - 6, 8, 8);
            gc.setFill(Color.rgb(150, 150, 150, 0.5));
            gc.setFont(Font.font("Arial", 11));
            gc.fillText("NO SHIELD", x + 28, bottomY + 2);
        }

        // SPEED (Enhanced bar with color coding)
        double currentSpeed = player.getSpeed();
        double baseSpeed = 400.0; // Normal speed
        double maxSpeedDef = 1000.0; // Max speed for gauge
        double speedRatio = Math.min(currentSpeed / maxSpeedDef, 1.0);
        double barW = 60;
        double barH = 4;
        double barX = x + cardW - barW - 15;

        gc.setFill(Color.GRAY);
        gc.fillRect(barX, bottomY - 2, barW, barH); // Background bar

        // Color-coded speed bar (Green = normal, Yellow = boosted)
        if (currentSpeed > baseSpeed) {
            // Speed boost active - yellow/gold
            gc.setFill(Color.GOLD);
        } else {
            // Normal speed - white
            gc.setFill(Color.WHITE);
        }
        gc.fillRect(barX, bottomY - 2, barW * speedRatio, barH); // Active bar

        gc.setFill(Color.rgb(200, 200, 200, 0.8));
        gc.setFont(Font.font("Arial", 9));
        gc.fillText("SPD", barX - 22, bottomY + 3);

        // Speed boost indicator
        if (currentSpeed > baseSpeed) {
            gc.setFill(Color.GOLD);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 8));
            gc.fillText("↑", barX + barW + 3, bottomY + 3);
        }

        gc.restore();
    }

    public void restartGame() {
        currentLevelIndex = 1;
        player.resetToInitialState();
        this.level = LevelFactory.createLevel(currentLevelIndex);
        controller.setLevel(level);
        setState(playingState);
    }

    // --- IMPLEMENTATION OBSERVER ---
    @Override
    public void onPlayerUpdate(int health, int score, int lvl) {
        this.displayedHealth = health;
        this.displayedScore = score;
    }

    // --- MÉTHODES UTILITAIRES POUR LES ÉTATS ---
    public Player getPlayer() {
        return player;
    }

    public GameController getController() {
        return controller;
    }

    public Level getLevel() {
        return level;
    }

    // Appelé par PlayingState

    // Gestion Input déléguée par RoboEscape.java
    public void onKeyPressed(javafx.scene.input.KeyCode code) {
        currentState.handleInput(code);
    }

    public void onKeyReleased(javafx.scene.input.KeyCode code) {
        currentState.handleKeyRelease(code);
    }

}