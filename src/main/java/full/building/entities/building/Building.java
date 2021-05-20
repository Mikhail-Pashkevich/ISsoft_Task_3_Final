package full.building.entities.building;

import full.building.entities.floor.Floor;
import full.building.entities.lift.Lift;
import full.building.entities.person.Person;
import lombok.Getter;
import lombok.Setter;
import full.building.service.FloorService;
import full.building.service.LiftService;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static full.building.entities.building.BuildingConstant.MAX_FLOOR;
import static full.building.entities.building.BuildingConstant.MAX_LIFT;
import static full.building.entities.lift.LiftConstant.MAX_WEIGHT;
import static java.lang.Math.random;


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

    public static Floor generateFloor(int floorNumber) {
        return new Floor(floorNumber, new ArrayDeque<>(), new ArrayDeque<>());
    }

    public static Lift generateLift(int maxWeight) {
        return new Lift(maxWeight);
    }

    public static Person generatePerson(int maxFloor) {
        return new Person((int) (random() * maxFloor), (int) (random() * 100 + 50));
    }

    public synchronized void init() {
        floors = new ArrayList<>(MAX_FLOOR);
        lifts = new ArrayList<>(MAX_LIFT);

        IntStream.range(0, MAX_FLOOR)
                .forEach(i -> floors.add(new FloorService(generateFloor(i))));
        IntStream.range(0, MAX_LIFT)
                .forEach(i -> lifts.add(new LiftService(generateLift(MAX_WEIGHT))));
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