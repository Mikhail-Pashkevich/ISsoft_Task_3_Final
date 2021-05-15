package entities;

import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.random;

@Getter
@Setter
public class Person {
    private final int totalFloor;
    private final double weight;

    public Person(int totalFloor, double weight) {
        this.totalFloor = totalFloor;
        this.weight = weight;
    }

    public static Person generatePerson(int maxFloor) {
        return new Person((int) (random() * maxFloor + 1), random() * 100 + 50);
    }

    public boolean isGoUp(int currentFloor) {
        return this.totalFloor > currentFloor;
    }

    public boolean isGoDown(int currentFloor) {
        return this.totalFloor < currentFloor;
    }
}
