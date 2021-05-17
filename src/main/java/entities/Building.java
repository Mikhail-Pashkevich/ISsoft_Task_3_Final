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
    private static volatile Building building;
    private static List<Floor> floors;
    private static List<Lift> lifts;

    public Building(List<Floor> floorList, List<Lift> liftList) {
        if (floors == null && lifts == null) {
            floors = floorList;
            lifts = liftList;
        }
    }

    public synchronized static Building generateBuilding(int maxFloor, int numberOfLifts) {
        List<Floor> floors = new ArrayList<>(maxFloor);
        List<Lift> lifts = new ArrayList<>(numberOfLifts);

        IntStream.range(0, maxFloor)
                .forEach(i -> floors.add(generateFloor(maxFloor, i)));
        IntStream.range(0, numberOfLifts)
                .forEach(i -> lifts.add(generateLift(maxFloor)));

        return new Building(floors, lifts);
    }

//    public void init(){
//                IntStream.range(0, maxFloor)
//                .forEach(i -> floors.add(generateFloor(maxFloor, i)));
//        IntStream.range(0, maxLift)
//                .forEach(i -> lifts.add(generateLift(maxFloor)));
//    }

    public static Building getBuilding(int maxFloor, int maxLift) {
        if (building == null) {
            building = generateBuilding(maxFloor, maxLift);
        }
        return building;
    }

    public void startWork() {
        IntStream.range(0, lifts.size())
                .forEach(i -> new Thread(lifts.get(i), "lift " + i).start());
        IntStream.range(0, floors.size())
                .forEach(i -> new Thread(floors.get(i), "floors " + i).start());
    }

    public Floor getFloor(int floorNumber) {
        return floors.get(floorNumber);
    }
    public Lift getLift(int liftNumber) {
        return lifts.get(liftNumber);
    }
}
