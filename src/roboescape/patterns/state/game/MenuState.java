package roboescape.patterns.state.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import roboescape.view.GameView;

public class MenuState implements GameState {

    private final GameView context;
    
    // Pour la navigation (0 = Jouer, 1 = Quitter)
    private int currentSelection = 0;
    private final String[] options = {"COMMENCER LA MISSION", "QUITTER LE SYSTÈME"};

    // Pour les effets visuels (Particules)
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();

    public MenuState(GameView context) {
        this.context = context;
        // Création de 50 particules au démarrage
        for (int i = 0; i < 50; i++) {
            particles.add(new Particle(random.nextDouble() * 800, random.nextDouble() * 600));
        }
    }

    @Override
    public void update() {
        // Mettre à jour les particules (les faire monter)
        for (Particle p : particles) {
            p.y -= p.speed;
            if (p.y < 0) {
                p.y = 600;
                p.x = random.nextDouble() * 800;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        double w = 800;
        double h = 600;

        // 1. FOND DÉGRADÉ PROFOND
        LinearGradient bg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(15, 15, 30)),
                new Stop(1, Color.rgb(5, 5, 20)));
        gc.setFill(bg);
        gc.fillRect(0, 0, w, h);

        // 2. DESSIN DES PARTICULES (Ambiance spatiale)
        gc.setFill(Color.rgb(100, 255, 255, 0.3));
        for (Particle p : particles) {
            gc.fillRect(p.x, p.y, p.size, p.size);
        }

        // 3. TITRE "ROBO ESCAPE"
        gc.setTextAlign(TextAlignment.CENTER);
        
        // Ombre portée du titre
        gc.setFill(Color.rgb(0, 255, 255, 0.2));
        gc.setFont(Font.font("Impact", 84)); // Police grasse
        gc.fillText("ROBO ESCAPE", w/2 + 4, 184);

        // Titre principal
        gc.setFill(Color.CYAN);
        gc.setFont(Font.font("Impact", 80));
        gc.fillText("ROBO ESCAPE", w/2, 180);
        
        // Sous-titre
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Courier New", 16));
        gc.fillText("PROTOCOL: ESCAPE // STATUS: READY", w/2, 210);

        // 4. MENU DE SÉLECTION
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        for (int i = 0; i < options.length; i++) {
            double yPos = 350 + (i * 60);
            
            if (i == currentSelection) {
                // Option sélectionnée (Brillante + Flèches)
                gc.setFill(Color.YELLOW);
                gc.fillText(">  " + options[i] + "  <", w/2, yPos);
                
                // Effet de lueur autour du texte sélectionné
                gc.setStroke(Color.ORANGE);
                gc.setLineWidth(1);
                gc.strokeText(">  " + options[i] + "  <", w/2, yPos);
            } else {
                // Option non sélectionnée (Grise)
                gc.setFill(Color.GRAY);
                gc.fillText(options[i], w/2, yPos);
            }
        }

        // 5. INSTRUCTIONS BAS DE PAGE
        gc.setFill(Color.rgb(100, 100, 100));
        gc.setFont(Font.font("Arial", 12));
        gc.fillText("Utilisez les FLÈCHES pour choisir et ENTRÉE pour valider", w/2, 550);
    }

    @Override
    public void handleInput(KeyCode code) {
        if (code == KeyCode.UP) {
            currentSelection--;
            if (currentSelection < 0) currentSelection = options.length - 1;
        } 
        else if (code == KeyCode.DOWN) {
            currentSelection++;
            if (currentSelection >= options.length) currentSelection = 0;
        } 
        else if (code == KeyCode.ENTER) {
            executeSelection();
        }
    }

    private void executeSelection() {
        if (currentSelection == 0) {
            // JOUER
            context.resetGame(); 
            context.setState(context.getPlayingState());
        } else if (currentSelection == 1) {
            // QUITTER
            System.exit(0);
        }
    }

    @Override
    public void handleKeyRelease(KeyCode code) {}

    // --- CLASSE INTERNE POUR LES PARTICULES ---
    private static class Particle {
        double x, y, speed, size;
        Particle(double x, double y) {
            this.x = x;
            this.y = y;
            this.speed = 0.5 + Math.random() * 1.5; // Vitesse variable
            this.size = 1 + Math.random() * 3;      // Taille variable
        }
    }
}