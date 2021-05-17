package entities;

import lombok.Getter;

import java.util.List;
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

    public int nearestForGoUp(int floorNumber) {
        int nearest = nearest(callUp.stream().toList(), floorNumber);
        System.out.println("nearest up " + nearest);
        return nearest;
    }

    public int nearestForGoDown(int floorNumber) {
        int nearest = nearest(callDown.stream().toList(), floorNumber);
        System.out.println("nearest down " + nearest);
        return nearest;
    }

    public int nearest(List<Integer> floors, int floorNumber) {
        if (!floors.isEmpty()) {
            if (floors.contains(floorNumber)) {
                return floorNumber;
            }
            if (floorNumber < floors.get(0)) {
                return floors.get(0);
            }
            if (floors.get(floors.size() - 1) < floorNumber) {
                return floors.get(floors.size() - 1);
            }

            for (int i = 0; i < floors.size() - 1; i++) {
                if (floors.get(i) < floorNumber && floorNumber < floors.get(i + 1)) {
                    if (Math.abs(floorNumber - floors.get(i)) < Math.abs(floorNumber - floors.get(i + 1))) {
                        return floors.get(i);
                    }
                    return floors.get(i + 1);
                }
            }
        }
        return -1;
    }

    public boolean isUpContain(int floorNumber) {
        return this.callUp.contains(floorNumber);
    }

    public boolean isDownContain(int floorNumber) {
        return this.callDown.contains(floorNumber);
    }

    public void addCallUp(int floorNumber) {
        this.callUp.add(floorNumber);
    }

    public void addCallDown(int floorNumber) {
        this.callDown.add(floorNumber);
    }

    public void removeCallUp(int floorNumber) {
        this.callUp.remove(floorNumber);
    }

    public void removeCallDown(int floorNumber) {
        this.callDown.remove(floorNumber);
    }

}
