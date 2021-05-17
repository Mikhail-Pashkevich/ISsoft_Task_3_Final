package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

import static constants.Const.QUEUE_SIZE;
import static constants.Const.WAITING_TIME_FOR_NEW_PERSON;
import static entities.Controller.getController;
import static entities.Person.generateAnyPerson;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

@Slf4j
@Getter
@Setter
public class Floor implements Runnable {
    private final int floorNumber;
    private final int maxFloor;
    private Deque<Person> queueUp;
    private Deque<Person> queueDown;
    //private static List<Lift> lifts;


    public Floor(int floorNumber, int maxFloor, Deque<Person> queueUp, Deque<Person> queueDown) {
        this.floorNumber = floorNumber;
        this.maxFloor = maxFloor;
        this.queueUp = queueUp;
        this.queueDown = queueDown;
    }

    public static Floor generateFloor(int maxFloor, int floorNumber) {
        return new Floor(floorNumber, maxFloor, new ArrayDeque<>(), new ArrayDeque<>());
    }

    @Override
    public void run() {
        Person person;

        while (true) {
            if (!queueUp.isEmpty()) {
                getController().addCallUp(floorNumber);
            }
            if (!queueDown.isEmpty()) {
                getController().addCallDown(floorNumber);
            }

            if (queueUp.size() + queueDown.size() < QUEUE_SIZE) {
                person = generateAnyPerson(maxFloor);


                if (person.isGoUp(floorNumber)) {
                    //TODO:remove
                    person.setName("person from " + currentThread().getName() + " go to " + person.getTotalFloor());
                    System.out.println(person.getName());
                    //
                    addToQueueUp(person);
                    getController().addCallUp(floorNumber);
                } else {
                    if (person.isGoDown(floorNumber)) {
                        //TODO:remove
                        person.setName("person from " + currentThread().getName() + " go to " + person.getTotalFloor());
                        System.out.println(person.getName());
                        //
                        addToQueueDown(person);
                        getController().addCallDown(floorNumber);
                    }
                }
            }
            waitNewPerson();
        }
    }


    public void addToQueueUp(Person person) {
        queueUp.addLast(person);
    }

    public void addToQueueDown(Person person) {
        queueDown.addLast(person);
    }

    public Optional<Person> getFromQueueUp() {
        queueDown.forEach(i -> System.out.println(i.getName() + " get"));
        if (!queueUp.isEmpty()) {
            System.out.println("get first up");
            return Optional.ofNullable(queueUp.getFirst());
        }
        return Optional.empty();
    }

    public Optional<Person> getFromQueueDown() {
        queueDown.forEach(i -> System.out.println(i.getName() + "get"));
        if (!queueDown.isEmpty()) {
            System.out.println("get first down");
            return Optional.ofNullable(queueDown.getFirst());
        }
        return Optional.empty();
    }

    public Optional<Person> removeFromQueueUp() {
        if (!queueUp.isEmpty()) {
            return Optional.ofNullable(queueUp.removeFirst());
        }
        return Optional.empty();
    }

    public Optional<Person> removeFromQueueDown() {
        if (!queueDown.isEmpty()) {
            return Optional.ofNullable(queueDown.removeFirst());
        }
        return Optional.empty();
    }

    private void waitNewPerson() {
        try {
            sleep(WAITING_TIME_FOR_NEW_PERSON);
        } catch (InterruptedException e) {
            log.info("can't put the thread to sleep", e);
        }
    }

}
