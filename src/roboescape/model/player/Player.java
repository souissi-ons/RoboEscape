package roboescape.model.player;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import roboescape.patterns.decorator.PowerUp;
import roboescape.patterns.state.PlayerState;
import roboescape.patterns.state.NormalState;
import roboescape.patterns.observer.GameObserver;
import roboescape.patterns.util.PatternLogger;

public class Player {

    // --- POSITION & TAILLE ---
    // On démarre au CENTRE (400, 300) pour être sûr de ne pas être dans un mur
    private double x = 400;
    private double y = 300;

    private double size = 40; // Taille de l'image (visuel)

    // --- HITBOX (PHYSIQUE) ---
    // On met une hitbox TRES petite (20px) pour être très agile
    private double hitboxSize = 28;
    private double hitboxOffset = (size - hitboxSize) / 2; // = 10px de marge
    private List<GameObserver> observers = new ArrayList<>();

    // --- GAMEPLAY ---
    // --- GAMEPLAY ---
    private double speed = 400.0; // Pixels par seconde
    private int health = 3;
    private int score = 0;
    private boolean shield = false;
    private boolean won = false;

    private boolean invulnerable = false;
    private long lastDamageTime = 0;

    private PlayerState currentState;
    private List<PowerUp> powerUps = new ArrayList<>();
    private Image robotSprite;

    public Player() {
        this.currentState = new NormalState(this);
        try {
            this.robotSprite = new Image("https://img.icons8.com/fluency/48/robot-2.png", true);
        } catch (Exception e) {
        }
    }

    // Dans Player.java
    public void render(GraphicsContext gc) {
        if (!isAlive()) {
            return; // Ne rien dessiner si mort
        }
        // 1. DESSINER LE ROBOT (IMAGE)
        if (robotSprite != null && !robotSprite.isError()) {
            gc.drawImage(robotSprite, x, y, size, size);
        } else {
            gc.setFill(currentState.getColor());
            gc.fillRect(x, y, size, size);
        }

        // 2. EFFET DÉGÂTS (Clignotement Rouge)
        // Si le joueur est invulnérable (vient de perdre une vie)
        if (invulnerable) {
            // Clignotement rapide (tous les 100ms)
            if (System.currentTimeMillis() % 200 < 100) {
                gc.setFill(Color.rgb(255, 0, 0, 0.5)); // Rouge semi-transparent
                gc.fillRect(x, y, size, size);
            }
        }

        // 3. DEBUG : VISUALISER LA COLLISION (Cadre Rouge)
        // C'est CE CADRE qui bloque contre les murs.
        // Tant que ce cadre ne touche pas le mur gris, tout va bien.
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.strokeRect(x + hitboxOffset, y + hitboxOffset, hitboxSize, hitboxSize);

        // 4. Bouclier
        if (shield) {
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(3);
            gc.strokeOval(x - 5, y - 5, size + 10, size + 10);
        }
    }

    public void update(double width, double height) {
        currentState.update();
        if (invulnerable && System.currentTimeMillis() - lastDamageTime > 1000) {
            invulnerable = false;
        }
        powerUps.forEach(p -> p.update(this));
        powerUps.removeIf(PowerUp::isExpired);
    }

    // --- GETTERS & SETTERS ---
    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    // Getters pour la physique
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize() {
        return size;
    }

    public double getHitboxSize() {
        return hitboxSize;
    }

    public double getHitboxOffset() {
        return hitboxOffset;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean hasWon() {
        return won;
    }

    public void setWon(boolean w) {
        this.won = w;
    }

    public void takeDamage(int amount) {
        if (invulnerable)
            return;
        this.health -= amount;
        this.invulnerable = true;
        this.lastDamageTime = System.currentTimeMillis();
        notifyObservers(); // <--- NOTIFICATION
    }

    public void resetPosition() {
        this.x = 400; // Retour au centre
        this.y = 300;
        this.won = false;
        notifyObservers();// On annule la victoire pour jouer le niveau suivant
        // On garde la vie et le score !
    }

    public void heal(int amount) {
        this.health += amount;
        notifyObservers(); // <--- NOTIFICATION
    }

    public void addScore(int points) {
        this.score += points;
        notifyObservers(); // <--- NOTIFICATION
    }

    public int getHealth() {
        return health;
    }

    public int getScore() {
        return score;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double s) {
        this.speed = s;
    }

    public void setCurrentState(PlayerState s) {
        String oldState = (currentState != null) ? currentState.getClass().getSimpleName() : "null";
        String newState = s.getClass().getSimpleName();
        PatternLogger.logStateTransition(oldState, newState, "Player");
        this.currentState = s;
    }

    public PlayerState getCurrentState() {
        return currentState;
    }

    public boolean hasShield() {
        return shield;
    }

    public void enableShield() {
        this.shield = true;
    }

    public void disableShield() {
        this.shield = false;
    }

    public void addPowerUp(PowerUp p) {
        p.apply(this);
        powerUps.add(p);
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        // On envoie (vie, score, 0) -> Le niveau sera géré par la Vue, on met 0 par
        // défaut ici
        PatternLogger.logObserverNotified("GameView", "PlayerUpdate",
                "Health=" + health, "Score=" + score);
        for (GameObserver obs : observers) {
            obs.onPlayerUpdate(health, score, 0);
        }
    }
}
