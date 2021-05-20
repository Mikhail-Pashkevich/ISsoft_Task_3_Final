package full.building.entities.lift;

import full.building.entities.building.Building;
import full.building.entities.controller.Controller;
import full.building.entities.person.Person;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static full.building.entities.building.Building.getBuilding;
import static full.building.entities.lift.LiftConstant.MAX_WEIGHT;
import static full.building.entities.lift.LiftState.*;
import static java.lang.Math.random;
import static org.junit.jupiter.api.Assertions.*;

class LiftTest {

    public static Person createAnyPerson() {
        return new Person((int) (random() * 3), (int) (random() * 100 + 50));
    }

    @Test
    void addActiveFloor_correct() {
        Controller controller = Controller.getController();
        controller.addCallUp(1);
        controller.addCallDown(2);
        Lift lift = createAnyLift();
        lift.setCurrentFloor(2);

        lift.addActiveFloor();

        assertEquals(2, lift.getActiveFloors().get(0));
    }

    @Test
    void activateFloor_correct() {
        Lift lift = createAnyLift();
        lift.setCurrentFloor(2);

        lift.activateFloor(2);

        assertTrue(lift.isActiveFloorsContainCurrent());
    }

    @Test
    void deactivateFloor_correct() {
        Lift lift = createAnyLift();
        lift.activateFloor(2);
        lift.setCurrentFloor(2);

        lift.deactivateFloor(2);

        assertFalse(lift.isActiveFloorsContainCurrent());
    }

    @Test
    void goToActiveFloor() {
        Lift lift = createAnyLift();
        lift.setCurrentFloor(0);
        lift.setActiveFloors(Collections.singletonList(2));

        lift.goToActiveFloor();

        assertEquals(2, lift.getCurrentFloor());
    }

    @Test
    void removePeopleForCurrent() {
        Person person_1 = createAnyPerson();
        person_1.setTotalFloor(2);
        Person person_2 = createAnyPerson();
        person_2.setTotalFloor(2);
        Lift lift = createAnyLift();
        lift.setCurrentFloor(2);
        lift.setPersonList(new ArrayList<>() {{
            add(person_1);
            add(person_2);
        }});

        lift.removePeopleForCurrent();

        assertTrue(lift.isLiftEmpty());
    }

    @Test
    void addPeopleForCurrent_GOING_DOWN() {
        Building building = getBuilding();
        building.getFloor(0).addToQueueDown(createAnyPerson());
        building.getLift(0).setCurrentFloor(0);

        building.getLift(0).addPeopleForCurrent(GOING_DOWN);

        assertTrue(building.getFloor(0).getQueueDown().isEmpty());
        assertFalse(building.getLift(0).getPersonList().isEmpty());
    }

    @Test
    void addPeopleForCurrent_GOING_UP() {
        Building building = getBuilding();
        building.getFloor(0).addToQueueUp(createAnyPerson());
        building.getLift(0).setCurrentFloor(0);

        building.getLift(0).addPeopleForCurrent(GOING_UP);

        assertTrue(building.getFloor(0).getQueueUp().isEmpty());
        assertFalse(building.getLift(0).getPersonList().isEmpty());
    }

    @Test
    public void getAction_STAND_CURRENT() {
        Lift lift = createAnyLiftWithActiveFloor();

        assertEquals(LiftState.STAND_CURRENT, lift.getAction());
    }

    @Test
    public void getAction_GOING_UP() {
        Lift lift = createAnyLiftWithActiveFloorAndPersonList();

        lift.setState(GOING_UP);

        assertEquals(GOING_UP, lift.getAction());
    }

    @Test
    public void getAction_GOING_DOWN() {
        Lift lift = createAnyLiftWithActiveFloorAndPersonList();

        lift.setState(GOING_DOWN);

        assertEquals(GOING_DOWN, lift.getAction());
    }

    @Test
    public void getAction_WAIT_ACTION() {
        Lift lift = createAnyLift();

        assertEquals(WAIT_ACTION, lift.getAction());
    }

    public Lift createAnyLift() {
        return new Lift(MAX_WEIGHT);
    }

    public Lift createAnyLiftWithActiveFloor() {
        Lift lift = createAnyLift();

        lift.setActiveFloors(Collections.singletonList(2));

        return lift;
    }

    public Lift createAnyLiftWithActiveFloorAndPersonList() {
        Lift lift = createAnyLift();

        lift.setActiveFloors(Collections.singletonList(2));
        lift.setPersonList(Collections.singletonList(createAnyPerson()));

        return lift;
    }
}