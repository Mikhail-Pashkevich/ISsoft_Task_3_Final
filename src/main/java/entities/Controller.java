package entities;

import lombok.Getter;

import java.util.Set;
import java.util.TreeSet;

@Getter
public class Controller {
    private static Controller controller;
    // saves the floor from which the call was made
    private final Set<Integer> callUp;
    private final Set<Integer> callDown;

    private Controller() {
        this.callUp = new TreeSet<>();
        this.callDown = new TreeSet<>();
    }

    public static Controller getController() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public int nearest(int floorNumber) {
        throw new UnsupportedOperationException();
    }

    public boolean isUpContain(int floorNumber) {
        return this.callUp.contains(floorNumber);
    }

    public boolean isDownContain(int floorNumber) {
        return this.callDown.contains(floorNumber);
    }

    public void addUpCall(int floorNumber) {
        this.callUp.add(floorNumber);
    }

    public void addDownCall(int floorNumber) {
        this.callDown.add(floorNumber);
    }
}
