package entities;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static constants.Const.MAX_FLOOR;
import static constants.Const.MAX_LIFT;
import static entities.Building.getBuilding;
import static entities.Direction.GO_DOWN;
import static entities.FloorTest.createAnyPerson;
import static org.junit.jupiter.api.Assertions.*;

class LiftTest {

    @Test
    public void getDirection_current() {
        Lift lift_maxFloor = createAnyLift();
        Lift lift_minFloor = createAnyLift();

        lift_maxFloor.setCurrentFloor(9);
        lift_minFloor.setCurrentFloor(2);

        assertEquals(Direction.CURRENT, lift_maxFloor.getAction());
        assertEquals(Direction.CURRENT, lift_minFloor.getAction());
    }
    @Test
    public void getDirection_goUp() {
        Lift lift = createAnyLift();

        lift.setCurrentFloor(1);

        assertEquals(Direction.GO_UP, lift.getAction());
    }
    @Test
    public void getDirection_goDown() {
        Lift lift = createAnyLift();

        lift.setCurrentFloor(10);

        assertEquals(GO_DOWN, lift.getAction());
    }

    @Test
    void addPeopleGoUp() {
        Building building = getBuilding(MAX_FLOOR, MAX_LIFT);

        building.getFloor(0).addToQueueDown(createAnyPerson());

        assertFalse(building.getFloor(0).getQueueDown().isEmpty());

        building.getLift(0).setCurrentFloor(0);
        building.getLift(0).addPeople(GO_DOWN);

        assertTrue(building.getFloor(0).getQueueDown().isEmpty());
        assertFalse(building.getLift(0).getPersonList().isEmpty());

    }



//    @Test
//    public void getDirection_wait() {
//        Lift lift = createAnyLift();
//
//        lift.setCurrentFloor(4);
//
//        assertEquals(Direction.WAIT, lift.getDirection());
//    }

    public Lift createAnyLift() {
        Lift lift = Lift.generateLift(10);

        lift.setActiveFloors(Arrays.asList(9, 5, 2, 7));

        return lift;
    }

}