package roboescape.patterns.state.game;

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

    private GameView context;
    private long startTime;

    public MenuState(GameView context) {
        this.context = context;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        // On pourrait animer des choses ici si besoin
    }

    @Override
    public void render(GraphicsContext gc) {
        double w = 800;
        double h = 600;

        // 1. FOND DÉGRADÉ (Cyberpunk Blue)
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(10, 10, 30)),
                new Stop(1, Color.rgb(20, 20, 60))
        );
        gc.setFill(gradient);
        gc.fillRect(0, 0, w, h);

        // 2. EFFET DE GRILLE (Retro Grid)
        gc.setStroke(Color.rgb(255, 255, 255, 0.05));
        gc.setLineWidth(1);
        for(int i=0; i<w; i+=40) gc.strokeLine(i, 0, i, h);
        for(int i=0; i<h; i+=40) gc.strokeLine(0, i, w, i);

        // 3. TITRE AVEC OMBRE 3D
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Verdana", FontWeight.BLACK, 70));
        
        // Ombre portée
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillText("ROBO ESCAPE", w/2 + 5, 155);
        
        // Texte principal (Cyan Néon)
        gc.setFill(Color.CYAN);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.fillText("ROBO ESCAPE", w/2, 150);
        gc.strokeText("ROBO ESCAPE", w/2, 150);

        // 4. DESSIN DU ROBOT (Au centre)
        drawRoboMascot(gc, w/2, 280);

        // 5. INSTRUCTIONS (Clignotement)
        // Animation simple : 1 seconde visible, 1 seconde invisible
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            gc.setFill(Color.YELLOW);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            gc.fillText("APPUYEZ SUR [ENTRÉE] POUR JOUER", w/2, 450);
        }

        // 6. SOUS-TEXTE
        gc.setFill(Color.GRAY);
        gc.setFont(Font.font("Arial", 16));
        gc.fillText("[ECHAP] pour Quitter", w/2, 500);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Courier New", 12));
        gc.fillText("v1.0 - Projet Design Patterns", w/2, 550);
    }

    // Petite méthode pour dessiner un robot stylé sur le menu
    private void drawRoboMascot(GraphicsContext gc, double x, double y) {
        double s = 3; // Échelle
        
        gc.save();
        gc.translate(x - 50, y - 50); // Centrage approximatif
        
        // Tête
        gc.setFill(Color.GRAY);
        gc.fillRect(10*s, 0*s, 14*s, 10*s);
        
        // Yeux (Rouges lumineux)
        gc.setFill(Color.RED);
        gc.fillOval(12*s, 2*s, 3*s, 3*s);
        gc.fillOval(19*s, 2*s, 3*s, 3*s);
        
        // Corps
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(5*s, 11*s, 24*s, 18*s);
        
        // Coeur (Réacteur)
        gc.setFill(Color.CYAN);
        gc.fillOval(14*s, 15*s, 6*s, 6*s);
        
        // Bras
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(4*s);
        gc.strokeLine(5*s, 13*s, 0*s, 20*s); // Bras gauche
        gc.strokeLine(29*s, 13*s, 34*s, 20*s); // Bras droit

        gc.restore();
    }

    @Override
    public void handleInput(KeyCode code) {
        if (code == KeyCode.ENTER) {
            // 1. On réinitialise le jeu au niveau 1
            context.resetGame(); 
            
            // 2. On change l'état pour "Jeu en cours"
            context.setState(context.getPlayingState()); 
            
        } else if (code == KeyCode.ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void handleKeyRelease(KeyCode code) {}
}