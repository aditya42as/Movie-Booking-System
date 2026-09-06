
import java.util.ArrayList;
import java.util.List;

class Cinema {

    private String name;
    private List<Screen> screens;

    public Cinema(String name) {
        this.name = name;
        this.screens = new ArrayList<>();
    }

    // Composition: Cinema creates/owns its screens.
    public void addScreen(int screenNumber) {
        screens.add(new Screen(screenNumber));
    }

    public String getName() {
        return name;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public Screen getScreen(int index) {
        if (index < 0 || index >= screens.size()) {
            return null;
        }
        return screens.get(index);
    }
}
