package entities;

import constants.Const;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static constants.Const.*;
import static entities.Building.getBuilding;
import static entities.Controller.getController;
import static entities.Direction.*;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

@Slf4j
@Getter
@Setter
public class Lift implements Runnable {
    private static Controller controller = getController();
    private static Building building = getBuilding(MAX_FLOOR, MAX_LIFT);
    private static int maxFloor;
    private Direction direction;
    private int currentFloor;
    private final double maxWeight;
    private double currentWeight;
    private List<Integer> activeFloors;
    private List<Person> personList;
    private LiftStatus status;

    public Lift(int lastFloor, double maxWeight) {
        maxFloor = lastFloor;
        direction = CURRENT;
        this.currentFloor = 0;
        this.maxWeight = maxWeight;
        this.currentWeight = 0;
        this.activeFloors = new ArrayList<>(maxFloor);
        this.personList = new ArrayList<>(maxFloor);
    }

    public static Lift generateLift(int maxFloor) {
        return new Lift(maxFloor, MAX_WEIGHT);
    }

    @Override
    public void run() {
        while (true) {
            if (isActiveFloorsEmpty()) {
                addActiveFloor();
                goToActiveFloor();
            }
            switch (getAction()) {
                case GO_UP -> {
                    if (activeFloors.contains(currentFloor)
                            || (controller.isUpContain(currentFloor) && currentWeight <= 0.9 * maxWeight)) {
                        controller.removeCallUp(currentFloor);
                        openDoor();
                        removePeopleForCurrent(currentFloor);
                        addPeople(GO_UP);
                        closeDoor();
                    }
                }
                case GO_DOWN -> {
                    if (activeFloors.contains(currentFloor)
                            || (controller.isDownContain(currentFloor) && currentWeight <= 0.9 * maxWeight)) {
                        controller.removeCallDown(currentFloor);
                        openDoor();
                        removePeopleForCurrent(currentFloor);
                        addPeople(GO_DOWN);
                        closeDoor();
                    }
                }
                case CURRENT -> {
                    controller.removeCallDown(currentFloor);
                    openDoor();
                    // because it not necessary
                    //removePeopleForCurrent(currentFloor);
                    addPeople(direction);
                    closeDoor();
                }
                case WAIT -> {
                    waitAction();
                }
            }
        }
    }

    public void addActiveFloor() {
        int forGoDown = controller.nearestForGoDown(currentFloor);
        int forGoUp = controller.nearestForGoUp(currentFloor);

        if (forGoDown < forGoUp && forGoDown != -1) {
            activeFloors.add(forGoUp);
            direction = GO_DOWN;
        } else {
            if (forGoUp < forGoDown && forGoUp != -1) {
                activeFloors.add(forGoUp);
                direction = GO_UP;
            } else {
                direction = WAIT;
            }
        }
    }
//            while (direction.equals(CURRENT)) {
//                // TODO: use wait, not sleep
//                waitAction();
//                // TODO: add synchronize method in controller because our lifts
//                // TODO: will go to same floors
//                System.out.println("block 1");
//                int forGoDown = controller.nearestForGoDown(currentFloor);
//                if (forGoDown != -1) {
//                    this.activeFloors.add(forGoDown);
//                    direction = GO_DOWN;
//                }
//                if (isActiveEmpty()) {
//                    int forGoUp = controller.nearestForGoUp(currentFloor);
//                    if (forGoUp != -1) {
//                        this.activeFloors.add(forGoUp);
//                        direction = GO_UP;
//                    }
//                }
//                if (!isActiveEmpty()) {
//                    goToActive();
//                    //////////////
//                    switch (direction) {
//                        case GO_UP -> {
//                            controller.removeCallUp(currentFloor);
//                            openDoor();
//                            addPeople(GO_UP);
//                            closeDoor();
//                        }
//                        case GO_DOWN -> {
//                            controller.removeCallDown(currentFloor);
//                            openDoor();
//                            addPeople(GO_DOWN);
//                            closeDoor();
//                        }
//                    }
//                } else {
//                    direction = CURRENT;
//                }
//            }
//
//            System.out.println("block 2");
//            System.out.println(activeFloors.toString());
//            System.out.println(personList.toString());
//            switch (getDirection()) {
//                case GO_UP -> {
//
//                    goUp();
//                    if (activeFloors.contains(currentFloor)) {
//                        // TODO: add removeFromQueue(int current)
//                        controller.removeCallUp(currentFloor);
//                        openDoor();
//                        removePeopleForCurrent(currentFloor);
//                        addPeople(GO_UP);
//                        closeDoor();
//                    } else {
//                        if (controller.isUpContain(currentFloor) && currentWeight <= 0.9 * maxWeight) {
//                            controller.getCallUp().remove(currentFloor);
//                            openDoor();
//                            addPeople(GO_UP);
//                            closeDoor();
//                        }
//                    }
//
//                }
//                case GO_DOWN -> {
//                    goDown();
//                    if (activeFloors.contains(currentFloor)) {
//                        // TODO: add removeFromQueue(int current)
//                        controller.removeCallDown(currentFloor);
//                        openDoor();
//                        removePeopleForCurrent(currentFloor);
//                        addPeople(GO_DOWN);
//                        closeDoor();
//                    } else {
//                        if (controller.isDownContain(currentFloor) && currentWeight <= 0.9 * maxWeight) {
//                            controller.getCallDown().remove(currentFloor);
//                            openDoor();
//                            addPeople(GO_DOWN);
//                            closeDoor();
//                        }
//                    }
//                }
//                case CURRENT -> {
//                    System.out.println("case current");
//                    if (currentWeight <= 0.9 * maxWeight) {
//                        switch (direction) {
//                            case GO_UP -> {
//                                openDoor();
//                                addPeopleGoUp(currentFloor);
//                                closeDoor();
//                            }
//                            case GO_DOWN -> {
//                                openDoor();
//                                addPeopleGoDown(currentFloor);
//                                closeDoor();
//                            }
//                        }
//                    }
//                }
    // }

//            throw new UnsupportedOperationException();
//    public boolean isOpen() {
//        throw new UnsupportedOperationException();
//    }
//
//    public boolean isUp() {
//        throw new UnsupportedOperationException();
//    }

    public void goUp() {
        System.out.println("go up current " + (currentFloor + 1));
        System.out.println(activeFloors.toString());
        this.currentFloor++;
        try {
            sleep(LIFT_SPEED);
        } catch (InterruptedException e) {
            log.info("can't go up", e);
        }
    }

    public void goDown() {
        System.out.println("go down current " + (currentFloor + 1));
        System.out.println(activeFloors.toString());
        this.currentFloor--;
        try {
            sleep(LIFT_SPEED);
        } catch (InterruptedException e) {
            log.info("can't go down", e);
        }
    }

    public void goToActiveFloor() {
        System.out.println("go to active");
        while (currentFloor != activeFloors.get(0)) {
            if (currentFloor < activeFloors.get(0)) {
                goUp();
            } else {
                goDown();
            }
        }
    }

    public void openDoor() {
        System.out.println("open door");
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

    public void removePeopleForCurrent(int numberFloor) {
        System.out.println("remove people");
        personList.forEach(person -> {
            if (person.getTotalFloor() == currentFloor) {
                currentWeight -= person.getWeight();
                System.out.println(person.getName() + " remove " + currentThread().getName());
            }

        });
        activeFloors.remove(Integer.valueOf(currentFloor));

        personList.removeIf(person -> person.getTotalFloor() == currentFloor);
    }

    public void addPeople(Direction direction) {
        System.out.println("add people to go down from " + currentFloor + " current weight = " + currentWeight);
        System.out.println(building.getFloor(currentFloor).getQueueDown().toString());
        System.out.println(personList.toString());

        Optional<Person> person = switch (direction) {
            case GO_UP -> building.getFloor(currentFloor).getFromQueueUp();
            case GO_DOWN -> building.getFloor(currentFloor).getFromQueueDown();
            default -> Optional.empty();
        };

        if (person.isPresent()) {
            System.out.println(person.get().getName() + currentThread().getName());
            if (person.get().getWeight() + currentWeight < maxWeight) {

                person = switch (direction) {
                    case GO_UP -> building.getFloor(currentFloor).removeFromQueueUp();
                    case GO_DOWN -> building.getFloor(currentFloor).removeFromQueueDown();
                    default -> Optional.empty();
                };
                if (person.isPresent()) {
                    System.out.println(person.get().getName() + currentThread().getName());

                    personList.add(person.get());
                    currentWeight += person.get().getWeight();
                    activeFloors.add(person.get().getTotalFloor());
                    System.out.println(person.get().getName() + " set to " + currentThread().getName());
                }
            }
        } else {
            System.out.println("can't find");
        }
    }

    public boolean isActiveFloorsEmpty() {
        return activeFloors.isEmpty();
    }

    public boolean isLiftEmpty() {
        return personList.isEmpty();
    }

    public void waitAction() {
        try {
            sleep(Const.WAIT_ACTION);
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

//        // if direction up than ...
//        int max = activeFloors.stream().max(Integer::compareTo).orElse(Integer.MAX_VALUE);
//
//        if (currentFloor == max) {
//            return CURRENT;
//        }
//        if (currentFloor > max) {
//            return GO_DOWN;
//        }
//
//        int min = activeFloors.stream().min(Integer::compareTo).orElse(Integer.MIN_VALUE);
//
//        if (currentFloor == min) {
//            return CURRENT;
//        }
//        if (currentFloor < min) {
//            return GO_UP;
//        }
//
//        if (currentFloor > min + (max - min) / 2) {
//            return GO_DOWN;
//        } else {
//            return GO_UP;
//        }
    }

}
