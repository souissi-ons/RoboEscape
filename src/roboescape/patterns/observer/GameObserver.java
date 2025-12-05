package roboescape.patterns.observer;

public interface GameObserver {
    void onPlayerUpdate(int health, int score, int level);
}