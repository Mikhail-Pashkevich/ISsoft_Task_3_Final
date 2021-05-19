package service;

import entities.controller.Controller;
import entities.lift.Lift;
import lombok.Getter;

import static entities.controller.Controller.getController;
import static entities.lift.Direction.GO_DOWN;
import static entities.lift.Direction.GO_UP;

public class LiftService implements Runnable {
    private static final Controller controller = getController();
    @Getter
    private final Lift lift;

    public LiftService(Lift lift) {
        this.lift = lift;
    }

    @Override
    public void run() {
        while (true) {
            if (lift.isActiveFloorsEmpty()) {
                lift.addActiveFloor();
                System.out.println("act flo " + lift.getActiveFloors().toString());
                lift.goToActiveFloor();
            }
            switch (lift.getAction()) {
                case GO_UP -> {
                    if (lift.isActiveFloorsContainCurrent()
                            || (controller.isUpContain(lift.getCurrentFloor()) && lift.getCurrentWeight() <= 0.9 * lift.getMaxWeight())) {
                        controller.removeCallUp(lift.getCurrentFloor());
                        lift.openDoor();
                        lift.removePeopleForCurrent();
                        lift.addPeople(GO_UP);
                        lift.closeDoor();
                    }
                    if (!lift.isLiftEmpty()) {
                        lift.goUp();
                    }
                }
                case GO_DOWN -> {
                    if (lift.isActiveFloorsContainCurrent()
                            || (controller.isDownContain(lift.getCurrentFloor()) && lift.getCurrentWeight() <= 0.9 * lift.getMaxWeight())) {
                        controller.removeCallDown(lift.getCurrentFloor());
                        lift.openDoor();
                        lift.removePeopleForCurrent();
                        lift.addPeople(GO_DOWN);
                        lift.closeDoor();
                    }
                    if (!lift.isLiftEmpty()) {
                        lift.goDown();
                    }
                }
                case CURRENT -> {
                    controller.removeCallDown(lift.getCurrentFloor());
                    lift.openDoor();
                    lift.addPeople(lift.getDirection());
                    lift.closeDoor();
                }
                case WAIT -> lift.waitAction();
            }
        }
    }
}