package entities;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


class PersonTest {
    private int numberOfFloor = 10;

    @Test
    void generatePerson() {
        Person anyPerson = Person.generatePerson(numberOfFloor);

        assertNotEquals(anyPerson.getTotalFloor(), 0);
        assertNotEquals(anyPerson.getTotalFloor(), numberOfFloor + 1);
    }

    @Test
    void f() {
        Set<Integer> set = new TreeSet<>();
    set.add(1);
    set.add(5);
    set.add(1);
    set.add(6);
    set.forEach(System.out::println);

//    System.out.println(set.stream().toList().get(0));
//        System.out.println(set.iterator().next());

//        set = new TreeSet<>(Comparator.reverseOrder());
//        set.add(1);
//        set.add(5);
//        set.add(2);
//        set.add(6);
//    set.forEach(System.out::println);

//    System.out.println(set.stream().toList().get(0));
//        System.out.println(set.iterator().next());

    }
}