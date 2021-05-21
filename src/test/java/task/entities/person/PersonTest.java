package task.entities.person;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonTest {
    private final int anyWeight = 10;

    @Test
    void isGoUp() {
        Person person = new Person(1, anyWeight);

        assertTrue(person.isGoUp(0));
    }

    @Test
    void isGoDown() {
        Person person = new Person(0, anyWeight);

        assertTrue(person.isGoDown(1));
    }
}