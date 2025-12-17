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
            resetGame();
        }
    }

    public void drawHUD(GraphicsContext gc) {
        // --- MODIFICATION DES COORDONNÉES X ---

        // 1. Fond semi-transparent
        // Avant : (10, 10, ...) -> Maintenant : (30, 10, ...)
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        gc.fillRect(30, 10, 200, 100);

        gc.setFill(Color.WHITE);

        // 2. Niveau (Reste à droite, pas de changement nécessaire)
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gc.fillText("LEVEL " + currentLevelIndex, 700, 30);

        // 3. Stats (Décalées vers la droite)
        // Avant : X=20 -> Maintenant : X=45
        gc.setFont(Font.font("Arial", 14));
        gc.fillText("Vitesse: " + String.format("%.1f", player.getSpeed()), 45, 30);

        // Bouclier
        if (player.hasShield()) {
            gc.setFill(Color.CYAN);
            gc.fillText("BOUCLIER: ACTIF", 45, 50);
        } else {
            gc.setFill(Color.GRAY);
            gc.fillText("Bouclier: Non", 45, 50);
        }

        // Vie (Mise à jour via Observer)
        gc.setFill(Color.rgb(255, 80, 80));
        gc.fillText("Vie: " + displayedHealth, 45, 70);

        // Score (Mise à jour via Observer)
        gc.setFill(Color.GOLD);
        gc.fillText("Score: " + displayedScore, 45, 90);
    }

    public void resetGame() {
        currentLevelIndex = 1;
        player.resetPosition();
        // Reset score/health manuellement si besoin, ou recréer un Player
        this.level = LevelFactory.createLevel(currentLevelIndex);
        controller.setLevel(level);
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