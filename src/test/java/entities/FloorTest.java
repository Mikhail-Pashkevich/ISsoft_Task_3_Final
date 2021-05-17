package entities;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FloorTest {
    @Test
    void addToQueueUp_correct() {
        Floor floor = createAnyFloor();
        Person anyPerson1 = createAnyPerson();
        Person anyPerson2 = createAnyPerson();

        floor.addToQueueUp(anyPerson1);
        floor.addToQueueUp(anyPerson2);

        assertEquals(floor.getQueueUp().size(), 2);
    }
    @Test
    void addToQueueDown_correct() {
        Floor floor = createAnyFloor();
        Person anyPerson1 = createAnyPerson();
        Person anyPerson2 = createAnyPerson();

        floor.addToQueueDown(anyPerson1);
        floor.addToQueueDown(anyPerson2);

        assertEquals(floor.getQueueDown().size(), 2);
    }

    @Test
    void getFromQueueUp_correct() {
        Floor floor = createAnyFloor();
        Person anyPerson1 = createAnyPerson();
        Person anyPerson2 = createAnyPerson();

        floor.addToQueueUp(anyPerson1);
        floor.addToQueueUp(anyPerson2);

        assertEquals(floor.getFromQueueUp().get(),anyPerson1);
        assertEquals(floor.getQueueUp().size(),2);
    }
    @Test
    void getFromQueueUp_empty() {
        Floor floor = createAnyFloor();

        assertEquals(floor.getFromQueueUp(), Optional.empty());
    }
    @Test
    void getFromQueueDown_empty() {
        Floor floor = createAnyFloor();

        assertEquals(floor.getFromQueueDown(), Optional.empty());
    }



    @Test
    void removeFromQueueUp_correct() {
        Floor floor = createAnyFloor();
        Person anyPerson1 = createAnyPerson();
        Person anyPerson2 = createAnyPerson();

        floor.addToQueueUp(anyPerson1);
        floor.addToQueueUp(anyPerson2);

        assertEquals(floor.removeFromQueueUp().get(),anyPerson1);
        assertEquals(floor.getFromQueueUp().get(),anyPerson2);
    }



    public static  Floor createAnyFloor() {
        return Floor.generateFloor(1, 1);
    }

    public static Person createAnyPerson() {
        return Person.generateAnyPerson(1);
    }


}