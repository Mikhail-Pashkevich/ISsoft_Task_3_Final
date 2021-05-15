package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static constants.Const.*;
import static entities.Controller.getController;
import static java.lang.Thread.sleep;

@Slf4j
@Getter
@Setter
public class Lift implements Runnable {
    private static int maxFloor;
    private int currentFloor;
    private final double maxWeight;
    private double currentWeight;
    private List<Integer> activeFloors;
    private List<Person> personList;

    public Lift(int lastFloor, double maxWeight) {
        maxFloor = lastFloor;
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
        if(this.activeFloors.isEmpty()){
            // add synchronize method in controller because our lifts
            // will go to same floors
            this.activeFloors.addAll(getController().getCallDown());
        }else{

        }


        throw new UnsupportedOperationException();
    }

    public boolean isOpen() {
        throw new UnsupportedOperationException();
    }

    public boolean isUp() {
        throw new UnsupportedOperationException();
    }

    public void goUp() {
        this.currentFloor++;
        try {
            sleep(LIFT_SPEED);
        } catch (InterruptedException e) {
            log.info("can't go up", e);
        }
    }

    public void goDown() {
        this.currentFloor--;
        try {
            sleep(LIFT_SPEED);
        } catch (InterruptedException e) {
            log.info("can't go down", e);
        }
    }

    public void openDoor() {
        try {
            sleep(OPEN_OR_CLOSE_DOOR);
        } catch (InterruptedException e) {
            log.info("can't open door", e);
        }
    }

    public void closeDoor() {
        try {
            sleep(OPEN_OR_CLOSE_DOOR);
        } catch (InterruptedException e) {
            log.info("can't close door", e);
        }
    }

    public void removePeopleForCurrent(int numberFloor) {
        throw new UnsupportedOperationException();
    }

    public void addPeopleForCurrent(int numberFloor) {
        throw new UnsupportedOperationException();
    }

}
