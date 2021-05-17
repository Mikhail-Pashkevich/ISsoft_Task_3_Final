package entities;

import org.junit.jupiter.api.Test;

import java.util.*;

import static entities.Person.generateAnyPerson;
import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    private int maxFloor = 10;

    @Test
    void generatePerson() {
        Person anyPerson = generateAnyPerson(maxFloor);

        assertNotEquals(anyPerson.getTotalFloor(), 0);
        assertNotEquals(anyPerson.getTotalFloor(), maxFloor + 1);
    }

    @Test
    void f() {
        Optional<Person> person = Optional.ofNullable(new Person(1, 1));
        System.out.println(person.isPresent());

    }



}