package entities.person;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static java.lang.Math.random;

@Getter
@ToString
public class Person {
    private final int totalFloor;
    private final int weight;
    @Setter
    private String name;

    public Person(int totalFloor, int weight) {
        this.totalFloor = totalFloor;
        this.weight = weight;
    }

    public static Person generateAnyPerson(int maxFloor) {
        return new Person((int) (random() * maxFloor), (int) (random() * 100 + 50));
    }

    public boolean isGoUp(int currentFloor) {
        return this.totalFloor > currentFloor;
    }

    public boolean isGoDown(int currentFloor) {
        return this.totalFloor < currentFloor;
    }
}