package task.entities.floor;

import task.entities.person.Person;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Optional;

import static java.lang.Math.random;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FloorTest {

    public static Floor createAnyFloor() {
        return new Floor(0, new ArrayDeque<>(), new ArrayDeque<>());
    }

    public static Person createAnyPerson() {
        return new Person((int) (random() * 3), (int) (random() * 100 + 50));
    }

    @Test
    void addToQueueUp_correct() {
        Floor floor = createAnyFloor();
        Person anyPerson = createAnyPerson();

        floor.addToQueueUp(anyPerson);

        assertEquals(1, floor.getQueueUp().size());
        assertEquals(anyPerson, floor.getQueueUp().getFirst());
    }

    @Test
    void addToQueueDown_correct() {
        Floor floor = createAnyFloor();
        Person anyPerson = createAnyPerson();

        floor.addToQueueDown(anyPerson);

        assertEquals(1, floor.getQueueDown().size());
        assertEquals(anyPerson, floor.getQueueDown().getFirst());
    }

    @Test
    void getFromQueueUp_correct() {
        Floor floor = createAnyFloor();
        Person anyPerson = createAnyPerson();

        floor.addToQueueUp(anyPerson);

        assertEquals(anyPerson, floor.getFromQueueUp().get());
        assertEquals(1, floor.getQueueUp().size());
    }

    @Test
    void getFromQueueUp_empty() {
        Floor floor = createAnyFloor();

        assertEquals(Optional.empty(), floor.getFromQueueUp());
    }

    @Test
    void getFromQueueDown_correct() {
        Floor floor = createAnyFloor();
        Person anyPerson = createAnyPerson();

        floor.addToQueueDown(anyPerson);

        assertEquals(anyPerson, floor.getFromQueueDown().get());
        assertEquals(1, floor.getQueueDown().size());
    }

    @Test
    void getFromQueueDown_empty() {
        Floor floor = createAnyFloor();

        assertEquals(Optional.empty(), floor.getFromQueueDown());
    }

    @Test
    void removeFromQueueUp_correct() {
        Floor floor = createAnyFloor();
        Person anyPerson_1 = createAnyPerson();
        Person anyPerson_2 = createAnyPerson();

        floor.addToQueueUp(anyPerson_1);
        floor.addToQueueUp(anyPerson_2);

        assertEquals(anyPerson_1, floor.removeFromQueueUp().get());
        assertEquals(anyPerson_2, floor.getFromQueueUp().get());
    }

    @Test
    void removeFromQueueDown_correct() {
        Floor floor = createAnyFloor();
        Person anyPerson_1 = createAnyPerson();
        Person anyPerson_2 = createAnyPerson();

        floor.addToQueueDown(anyPerson_1);
        floor.addToQueueDown(anyPerson_2);

        assertEquals(anyPerson_1, floor.removeFromQueueDown().get());
        assertEquals(anyPerson_2, floor.getFromQueueDown().get());
    }
}