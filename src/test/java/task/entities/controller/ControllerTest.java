package task.entities.controller;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerTest {

    @Test
    public void nearestForGoUp_correct() {
        Controller controller = createAnyController();

        assertEquals(5, controller.nearestForGoUp(4));
        assertEquals(5, controller.nearestForGoUp(6));
        assertEquals(8, controller.nearestForGoUp(7));
    }

    @Test
    public void nearestForGoDown_correct() {
        Controller controller = createAnyController();

        assertEquals(7, controller.nearestForGoDown(5));
        assertEquals(2, controller.nearestForGoDown(4));
    }

    @Test
    void nearest_correct() {
        Controller controller = createAnyController();

        assertEquals(1, controller.nearest(asList(1, 3, 6, 7), 0));
        assertEquals(3, controller.nearest(asList(1, 3, 6, 7), 4));
        assertEquals(6, controller.nearest(asList(1, 3, 6, 7), 6));
        assertEquals(7, controller.nearest(asList(1, 3, 6, 7), 8));
    }

    public Controller createAnyController() {
        Controller controller = Controller.getController();

        controller.addCallUp(3);
        controller.addCallUp(5);
        controller.addCallUp(8);
        controller.addCallDown(7);
        controller.addCallDown(2);

        return controller;
    }
}