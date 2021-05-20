package full.building.service;

import full.building.entities.controller.Controller;
import full.building.entities.lift.Lift;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static full.building.entities.controller.Controller.getController;
import static full.building.entities.lift.LiftState.GOING_DOWN;
import static full.building.entities.lift.LiftState.GOING_UP;


@Slf4j
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
                lift.goToActiveFloor();
            }
            switch (lift.getAction()) {
                case GOING_UP -> {
                    if (lift.isActiveFloorsContainCurrent()
                            || (controller.isUpContain(lift.getCurrentFloor()) && lift.getCurrentWeight() <= 0.9 * lift.getMaxWeight())) {
                        controller.removeCallUp(lift.getCurrentFloor());
                        lift.openDoor();
                        lift.removePeopleForCurrent();
                        lift.addPeopleForCurrent(GOING_UP);
                        lift.closeDoor();
                    }
                    if (!lift.isLiftEmpty()) {
                        lift.goUp();
                    }
                }
                case GOING_DOWN -> {
                    if (lift.isActiveFloorsContainCurrent()
                            || (controller.isDownContain(lift.getCurrentFloor()) && lift.getCurrentWeight() <= 0.9 * lift.getMaxWeight())) {
                        controller.removeCallDown(lift.getCurrentFloor());
                        lift.openDoor();
                        lift.removePeopleForCurrent();
                        lift.addPeopleForCurrent(GOING_DOWN);
                        lift.closeDoor();
                    }
                    if (!lift.isLiftEmpty()) {
                        lift.goDown();
                    }
                }
                case STAND_CURRENT -> {
                    controller.removeCallDown(lift.getCurrentFloor());
                    lift.openDoor();
                    lift.addPeopleForCurrent(lift.getState());
                    lift.closeDoor();
                }
                case WAIT_ACTION -> lift.waitAction();
            }
        }
    }
}