package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static constants.Const.QUEUE_SIZE;
import static constants.Const.WAITING_TIME_FOR_NEW_PERSON;
import static entities.Controller.getController;
import static entities.Person.generatePerson;
import static java.lang.Thread.sleep;

@Slf4j
@Getter
@Setter
public class Floor implements Runnable {
    private final int floorNumber;
    private final int maxFloor;
    private Deque<Person> queueUp;
    private Deque<Person> queueDown;
    private static List<Lift> lifts;


    public Floor(int floorNumber, int maxFloor, Deque<Person> queueUp, Deque<Person> queueDown) {
        this.floorNumber = floorNumber;
        this.maxFloor = maxFloor;
        this.queueUp = queueUp;
        this.queueDown = queueDown;
    }
    public static Floor generateFloor(int maxFloor, int floorNumber) {
        return new Floor(floorNumber,maxFloor,new ArrayDeque<>(),new ArrayDeque<>());
    }

    @Override
    public void run() {
        Person person;

        while (true) {
            if(!queueUp.isEmpty()){
                getController().addUpCall(floorNumber);
            }
            if(!queueDown.isEmpty()){
                getController().addDownCall(floorNumber);
            }

            if (queueUp.size() + queueDown.size() < QUEUE_SIZE) {
                person = generatePerson(maxFloor);

                if (person.isGoUp(floorNumber)) {
                    addToQueueUp(person);
                    getController().addUpCall(floorNumber);
                } else {
                    if (person.isGoDown(floorNumber)) {
                        addToQueueDown(person);
                        getController().addDownCall(floorNumber);
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

    private void waitNewPerson() {
        try {
            sleep(WAITING_TIME_FOR_NEW_PERSON);
        } catch (InterruptedException e) {
            log.info("can't put the thread to sleep", e);
        }
    }

}
