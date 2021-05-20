package full.building.entities.person;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Person {
    private final int weight;
    private int totalFloor;
    private String name;

    public Person(int totalFloor, int weight) {
        this.totalFloor = totalFloor;
        this.weight = weight;
    }

    public boolean isGoUp(int currentFloor) {
        return this.totalFloor > currentFloor;
    }

    public boolean isGoDown(int currentFloor) {
        return this.totalFloor < currentFloor;
    }
}