package entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerTest {

    @Test
    public void nearestForGoUp() {
        Controller controller = createAnyController();

        assertEquals(controller.nearestForGoUp(4), 5);
        assertEquals(controller.nearestForGoUp(6), 5);
        assertEquals(controller.nearestForGoUp(7), 8);
    }

    public Controller createAnyController() {
        Controller controller = Controller.getController();

        controller.addCallUp(3);
        controller.addCallUp(5);
        controller.addCallUp(8);

        return controller;
    }
}