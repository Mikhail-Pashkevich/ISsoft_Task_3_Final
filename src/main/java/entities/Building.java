package entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static entities.Floor.generateFloor;
import static entities.Lift.generateLift;


@Getter
@Setter
public class Building {
    private final List<Floor> floors;
    private final List<Lift> lifts;

    public Building(List<Floor> floors, List<Lift> lifts) {
        this.floors = floors;
        this.lifts = lifts;
    }

    public Building generateBuilding(int maxFloor, int numberOfLifts) {
        List<Floor> floors = new ArrayList<>(maxFloor);
        List<Lift> lifts = new ArrayList<>(numberOfLifts);

        IntStream.range(0, maxFloor)
                .forEach(i -> floors.add(generateFloor(maxFloor, i)));
        IntStream.range(0, numberOfLifts)
                .forEach(i -> lifts.add(generateLift(maxFloor)));

        return new Building(floors, lifts);
    }

    public void startWork() {
        IntStream.range(0, lifts.size())
                .forEach(i -> new Thread(lifts.get(i), "lift " + i).start());
        IntStream.range(0, floors.size())
                .forEach(i -> new Thread(floors.get(i), "floors " + i).start());
    }

    public void liftHandler() {

    }
}
