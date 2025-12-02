package roboescape.patterns.state;

import javafx.scene.paint.Color;

public interface PlayerState {
    void update();
    void onEnter(); 
    Color getColor(); 
}
