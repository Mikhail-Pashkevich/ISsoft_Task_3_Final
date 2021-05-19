package entities.floor;

import entities.person.Person;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.Optional;

import static entities.floor.FloorConstant.WAITING_TIME_FOR_NEW_PERSON;
import static java.lang.Thread.sleep;

@Slf4j
@Getter
public class Floor {
    private final int floorNumber;
    private final int maxFloor;
    private final Deque<Person> queueUp;
    private final Deque<Person> queueDown;

    public Floor(int floorNumber, int maxFloor, Deque<Person> queueUp, Deque<Person> queueDown) {
        this.floorNumber = floorNumber;
        this.maxFloor = maxFloor;
        this.queueUp = queueUp;
        this.queueDown = queueDown;
    }


    public boolean isEmptyQueueUp() {
        return queueUp.isEmpty();
    }

    public boolean isEmptyQueueDown() {
        return queueDown.isEmpty();
    }

    public void addToQueueUp(Person person) {
        queueUp.addLast(person);
    }

    public void addToQueueDown(Person person) {
        queueDown.addLast(person);
    }

    public synchronized Optional<Person> getFromQueueUp() {
        queueDown.forEach(i -> System.out.println(i.getName() + " get"));
        if (!queueUp.isEmpty()) {
            System.out.println("get first up");
            return Optional.ofNullable(queueUp.getFirst());
        }
        return Optional.empty();
    }

    public synchronized Optional<Person> getFromQueueDown() {
        queueDown.forEach(i -> System.out.println(i.getName() + "get"));
        if (!queueDown.isEmpty()) {
            System.out.println("get first down");
            return Optional.ofNullable(queueDown.getFirst());
        }
        return Optional.empty();
    }

    public synchronized Optional<Person> removeFromQueueUp() {
        if (!queueUp.isEmpty()) {
            return Optional.ofNullable(queueUp.removeFirst());
        }
        return Optional.empty();
    }

    public synchronized Optional<Person> removeFromQueueDown() {
        if (!queueDown.isEmpty()) {
            return Optional.ofNullable(queueDown.removeFirst());
        }
        return Optional.empty();
    }

    public void waitNewPerson() {
        try {
            sleep(WAITING_TIME_FOR_NEW_PERSON);
        } catch (InterruptedException e) {
            log.info("can't put the thread to sleep", e);
        }
    }
}