package entities.lift;

import entities.building.Building;
import entities.controller.Controller;
import entities.person.Person;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static entities.building.Building.getBuilding;
import static entities.controller.Controller.getController;
import static entities.lift.Direction.*;
import static entities.lift.LiftConstant.*;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

@Slf4j
@Getter
@Setter
public class Lift {
    private static Controller controller = getController();
    private static Building building = getBuilding();
    private final double maxWeight;
    private int maxFloor;
    private int maxLift;
    private Direction direction;
    private int currentFloor;
    private double currentWeight;
    private List<Integer> activeFloors;
    private List<Person> personList;


    public Lift(int maxFloor, double maxWeight, int maxLift) {
        this.maxFloor = maxFloor;
        this.maxLift = maxLift;
        this.direction = CURRENT;
        this.currentFloor = 0;
        this.maxWeight = maxWeight;
        this.currentWeight = 0;
        this.activeFloors = new ArrayList<>(maxFloor);
        this.personList = new ArrayList<>();
    }

    public void addActiveFloor() {
        Controller controller = getController();

        int forGoDown = controller.nearestForGoDown(currentFloor);
        int forGoUp = controller.nearestForGoUp(currentFloor);
        int nearest = controller.nearest(Arrays.asList(forGoDown, forGoUp), currentFloor);

        if (nearest == forGoDown && nearest != -1) {
            activateFloor(forGoDown);
            direction = GO_DOWN;
            return;
        }
        if (nearest == forGoUp && nearest != -1) {
            activateFloor(forGoUp);
            direction = GO_UP;
            return;
        }

        direction = WAIT;
    }

    public void activateFloor(int floorNumber) {
        if (!activeFloors.contains(floorNumber)) {
            activeFloors.add(floorNumber);
        }
    }

    public void deactivateFloor(int floorNumber) {
        activeFloors.remove(Integer.valueOf(floorNumber));
    }

    public void goUp() {
        System.out.println("go up current " + (currentFloor + 1));
        System.out.println("act flo " + activeFloors.toString());
        this.currentFloor++;
        try {
            sleep(LIFT_SPEED);
        } catch (InterruptedException e) {
            log.info("can't go up", e);
        }
    }

    public void goDown() {
        System.out.println("go down current " + (currentFloor + 1));
        System.out.println("act flo " + activeFloors.toString());
        this.currentFloor--;
        try {
            sleep(LIFT_SPEED);
        } catch (InterruptedException e) {
            log.info("can't go down", e);
        }
    }

    public void goToActiveFloor() {
        System.out.println("go to active");
        if (!isActiveFloorsEmpty()) {
            while (currentFloor != activeFloors.get(0)) {
                if (currentFloor < activeFloors.get(0)) {
                    goUp();
                } else {
                    goDown();
                }
            }
        }
    }

    public void openDoor() {
        System.out.println("open door");
        deactivateFloor(currentFloor);
        try {
            sleep(OPEN_OR_CLOSE_DOOR);
        } catch (InterruptedException e) {
            log.info("can't open door", e);
        }
    }

    public void closeDoor() {
        System.out.println("close door");
        try {
            sleep(OPEN_OR_CLOSE_DOOR);
        } catch (InterruptedException e) {
            log.info("can't close door", e);
        }
    }

    public void removePeopleForCurrent() {
        System.out.println("remove people");
        personList.forEach(person -> {
            if (person.getTotalFloor() == currentFloor) {
                currentWeight -= person.getWeight();
                System.out.println(person.getName() + " remove " + currentThread().getName());
            }
        });

        personList.removeIf(person -> person.getTotalFloor() == currentFloor);
    }

    public void addPeople(Direction direction) {
        Building building = getBuilding();

        System.out.println("add people to go from " + currentFloor + " current weight = " + currentWeight);
        System.out.println("queue up " + building.getFloor(currentFloor).getQueueUp().toString());
        System.out.println("queue down " + building.getFloor(currentFloor).getQueueDown().toString());
        System.out.println("person list " + personList.toString());
        while (true) {

            Optional<Person> person = switch (direction) {
                case GO_UP -> building.getFloor(currentFloor).getFromQueueUp();
                case GO_DOWN -> building.getFloor(currentFloor).getFromQueueDown();
                default -> Optional.empty();
            };

            if (person.isPresent() && person.get().getWeight() + currentWeight < maxWeight) {

                person = switch (direction) {
                    case GO_UP -> building.getFloor(currentFloor).removeFromQueueUp();
                    case GO_DOWN -> building.getFloor(currentFloor).removeFromQueueDown();
                    default -> Optional.empty();
                };
                if (person.isPresent()) {
                    personList.add(person.get());
                    currentWeight += person.get().getWeight();
                    activateFloor(person.get().getTotalFloor());
                    System.out.println(person.get().getName() + " set to " + currentThread().getName());
                }
            } else {
                System.out.println("can't find");
                return;
            }
        }
    }

    public boolean isActiveFloorsEmpty() {
        return activeFloors.isEmpty();
    }

    public boolean isActiveFloorsContainCurrent() {
        return activeFloors.contains(currentFloor);
    }

    public boolean isLiftEmpty() {
        return personList.isEmpty();
    }

    public void waitAction() {
        try {
            sleep(WAIT_ACTION);
        } catch (InterruptedException e) {
            log.info("can't wait new action", e);
        }
    }

    public Direction getAction() {
        if (!isLiftEmpty()) {
            return direction;
        } else {
            if (!isActiveFloorsEmpty()) {
                return CURRENT;
            } else {
                return WAIT;
            }
        }
    }
}