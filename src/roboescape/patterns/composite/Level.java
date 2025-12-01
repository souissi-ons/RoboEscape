package roboescape.patterns.composite;

import java.util.ArrayList;
import java.util.List;

public class Level implements LevelComponent {

    private List<LevelComponent> components = new ArrayList<>();

    public void add(LevelComponent comp) {
        components.add(comp);
    }

    @Override
    public void render() {
        for (LevelComponent c : components)
            c.render();
    }
}
