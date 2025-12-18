package roboescape.model;

import java.io.*;

/**
 * Singleton gestionnaire de High Score.
 * Sauvegarde et charge le meilleur score depuis un fichier.
 */
public class HighScoreManager {

    private static HighScoreManager instance;
    private static final String FILE_NAME = "highscore.dat";
    private int highScore = 0;

    private HighScoreManager() {
        loadHighScore();
    }

    public static HighScoreManager getInstance() {
        if (instance == null) {
            instance = new HighScoreManager();
        }
        return instance;
    }

    public int getHighScore() {
        return highScore;
    }

    /**
     * Vérifie si le score actuel est un record.
     * Si oui, le sauvegarde.
     * @param score Score actuel
     * @return true si nouveau record
     */
    public boolean checkAndSetHighScore(int score) {
        if (score > highScore) {
            highScore = score;
            saveHighScore();
            return true;
        }
        return false;
    }

    private void saveHighScore() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(FILE_NAME))) {
            dos.writeInt(highScore);
            System.out.println("High Score saved: " + highScore);
        } catch (IOException e) {
            System.err.println("Error saving high score: " + e.getMessage());
        }
    }

    private void loadHighScore() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            highScore = dis.readInt();
            System.out.println("High Score loaded: " + highScore);
        } catch (IOException e) {
            System.err.println("Error loading high score: " + e.getMessage());
        }
    }
}
