package full.building.entities.lift;

import full.building.entities.building.Building;
import full.building.entities.controller.Controller;
import full.building.entities.person.Person;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static full.building.entities.building.Building.getBuilding;
import static full.building.entities.controller.Controller.getController;
import static full.building.entities.lift.LiftConstant.*;
import static full.building.entities.lift.LiftState.*;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

@Slf4j
@Getter
@Setter
public class Lift {
    private final double maxWeight;
    private LiftState state;
    private int currentFloor;
    private double currentWeight;
    private List<Integer> activeFloors;
    private List<Person> personList;


    public Lift(double maxWeight) {
        this.state = STAND_CURRENT;
        this.currentFloor = 0;
        this.maxWeight = maxWeight;
        this.currentWeight = 0;
        this.activeFloors = new ArrayList<>();
        this.personList = new ArrayList<>();
    }

    public void addActiveFloor() {
        Controller controller = getController();

        int forGoDown = controller.nearestForGoDown(currentFloor);
        int forGoUp = controller.nearestForGoUp(currentFloor);
        int nearest = controller.nearest(Arrays.asList(forGoDown, forGoUp), currentFloor);

        if (nearest == forGoDown && nearest != -1) {
            activateFloor(forGoDown);
            state = GOING_DOWN;
            return;
        }
        if (nearest == forGoUp && nearest != -1) {
            activateFloor(forGoUp);
            state = GOING_UP;
            return;
        }

        state = LiftState.WAIT_ACTION;
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
        log.info(currentThread().getName() + ": go up from " + currentFloor);
        this.currentFloor++;
        try {
            sleep(LIFT_SPEED);
        } catch (InterruptedException e) {
            log.error("can't go up", e);
        }
    }

    public void goDown() {
        log.info(currentThread().getName() + ": go down from " + currentFloor);
        this.currentFloor--;
        try {
            sleep(LIFT_SPEED);
        } catch (InterruptedException e) {
            log.error("can't go down", e);
        }
    }

    public void goToActiveFloor() {
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
        log.info(currentThread().getName() + ": open door");
        deactivateFloor(currentFloor);
        try {
            sleep(OPEN_OR_CLOSE_DOOR);
        } catch (InterruptedException e) {
            log.error("can't open door", e);
        }
    }

    public void closeDoor() {
        log.info(currentThread().getName() + ": close door");
        try {
            sleep(OPEN_OR_CLOSE_DOOR);
        } catch (InterruptedException e) {
            log.error("can't close door", e);
        }
    }

    public void removePeopleForCurrent() {
        personList.forEach(person -> {
            if (person.getTotalFloor() == currentFloor) {
                currentWeight -= person.getWeight();
                log.info(currentThread().getName() + ": remove " + person.getName());
            }
        });

        personList.removeIf(person -> person.getTotalFloor() == currentFloor);
    }

    public void addPeopleForCurrent(LiftState state) {
        Building building = getBuilding();

        log.info(currentThread().getName() + ": add people to go from " + currentFloor);
        log.info(currentThread().getName() + ": current person list " + personList.toString());
        while (true) {

            Optional<Person> person = switch (state) {
                case GOING_UP -> building.getFloor(currentFloor).getFromQueueUp();
                case GOING_DOWN -> building.getFloor(currentFloor).getFromQueueDown();
                default -> Optional.empty();
            };

            if (person.isPresent() && person.get().getWeight() + currentWeight < maxWeight) {

                person = switch (state) {
                    case GOING_UP -> building.getFloor(currentFloor).removeFromQueueUp();
                    case GOING_DOWN -> building.getFloor(currentFloor).removeFromQueueDown();
                    default -> Optional.empty();
                };
                if (person.isPresent()) {
                    personList.add(person.get());
                    currentWeight += person.get().getWeight();
                    activateFloor(person.get().getTotalFloor());
                    log.info(currentThread().getName()+": "+person.get().getName() + " set"  );
                }
            } else {
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
            sleep(WAIT_ACTION_FROM_CONTROLLER);
        } catch (InterruptedException e) {
            log.error("can't wait new action", e);
        }
    }

    public LiftState getAction() {
        if (!isLiftEmpty()) {
            return state;
        } else {
            if (!isActiveFloorsEmpty()) {
                return STAND_CURRENT;
            } else {
                return LiftState.WAIT_ACTION;
            }
        }
    }
}