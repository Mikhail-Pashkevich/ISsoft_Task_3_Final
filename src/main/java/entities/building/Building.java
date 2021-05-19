package entities.building;

import entities.floor.Floor;
import entities.lift.Lift;
import lombok.Getter;
import lombok.Setter;
import service.FloorService;
import service.LiftService;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static entities.building.BuildingConstant.MAX_FLOOR;
import static entities.building.BuildingConstant.MAX_LIFT;
import static entities.lift.LiftConstant.MAX_WEIGHT;


@Getter
@Setter
public class Building {
    private static Building building;

    private List<FloorService> floors;
    private List<LiftService> lifts;


    public Building() {
    }

    public static Building getBuilding() {
        if (building == null) {
            building = new Building();
            building.init();
        }
        return building;
    }

    public static Floor generateFloor(int maxFloor, int floorNumber) {
        return new Floor(floorNumber, maxFloor, new ArrayDeque<>(), new ArrayDeque<>());
    }

    public static Lift generateLift(int maxFloor, int maxWeight, int maxLift) {
        return new Lift(maxFloor, maxWeight, maxLift);
    }

    public synchronized void init() {
        floors = new ArrayList<>(MAX_FLOOR);
        lifts = new ArrayList<>(MAX_LIFT);

        IntStream.range(0, MAX_FLOOR)
                .forEach(i -> floors.add(new FloorService(generateFloor(MAX_FLOOR, i))));
        IntStream.range(0, MAX_LIFT)
                .forEach(i -> lifts.add(new LiftService(generateLift(MAX_LIFT, MAX_WEIGHT, MAX_LIFT))));
    }

    public void startWork() {
        IntStream.range(0, lifts.size())
                .forEach(i -> new Thread(lifts.get(i), "lift " + i).start());
        IntStream.range(0, floors.size())
                .forEach(i -> new Thread(floors.get(i), "floor " + i).start());
    }

    public Floor getFloor(int floorNumber) {
        return floors.get(floorNumber).getFloor();
    }

    public Lift getLift(int liftNumber) {
        return lifts.get(liftNumber).getLift();
    }
}