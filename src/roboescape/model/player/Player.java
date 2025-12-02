package roboescape.model.player;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import roboescape.patterns.decorator.PowerUp;
import roboescape.patterns.state.PlayerState;
import roboescape.patterns.state.NormalState;

public class Player {

    // --- POSITION & TAILLE ---
    // On démarre au CENTRE (400, 300) pour être sûr de ne pas être dans un mur
    private double x = 400; 
    private double y = 300;
    
    private double size = 40; // Taille de l'image (visuel)
    
    // --- HITBOX (PHYSIQUE) ---
    // On met une hitbox TRES petite (20px) pour être très agile
    private double hitboxSize = 20; 
    private double hitboxOffset = (size - hitboxSize) / 2; // = 10px de marge

    // --- GAMEPLAY ---
    private double speed = 3;
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
        } catch (Exception e) {}
    }

    public void render(GraphicsContext gc) {
        if (!isAlive()) return;

        // 1. DESSINER LE ROBOT (IMAGE)
        if (robotSprite != null && !robotSprite.isError()) {
            gc.drawImage(robotSprite, x, y, size, size);
        } else {
            gc.setFill(currentState.getColor());
            gc.fillRect(x, y, size, size);
        }

        // 2. DEBUG : DESSINER LA HITBOX (LE CARRÉ ROUGE)
        // C'est ce carré qui ne doit pas toucher les murs !
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.strokeRect(x + hitboxOffset, y + hitboxOffset, hitboxSize, hitboxSize);

        // 3. Bouclier
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
        this.x += dx * speed;
        this.y += dy * speed;
    }

    // Getters pour la physique
    public double getX() { return x; }
    public double getY() { return y; }
    public double getSize() { return size; }
    public double getHitboxSize() { return hitboxSize; }
    public double getHitboxOffset() { return hitboxOffset; }

    public boolean isAlive() { return health > 0; }
    public boolean hasWon() { return won; }
    public void setWon(boolean w) { this.won = w; }
    
    public void takeDamage(int amount) {
        if (invulnerable) return;
        this.health -= amount;
        this.invulnerable = true;
        this.lastDamageTime = System.currentTimeMillis();
    }
    
    public void heal(int amount) { health += amount; }
    public void addScore(int pts) { score += pts; }
    public int getHealth() { return health; }
    public int getScore() { return score; }
    
    public double getSpeed() { return speed; }
    public void setSpeed(double s) { this.speed = s; }
    public void setCurrentState(PlayerState s) { this.currentState = s; }
    public PlayerState getCurrentState() { return currentState; }
    public boolean hasShield() { return shield; }
    public void enableShield() { this.shield = true; }
    public void disableShield() { this.shield = false; }
    public void addPowerUp(PowerUp p) { p.apply(this); powerUps.add(p); }
}