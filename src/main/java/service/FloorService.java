package service;

import entities.controller.Controller;
import entities.floor.Floor;
import entities.person.Person;
import lombok.Getter;

import static entities.controller.Controller.getController;
import static entities.floor.FloorConstant.QUEUE_SIZE;
import static entities.person.Person.generateAnyPerson;
import static java.lang.Thread.currentThread;

public class FloorService implements Runnable {
    private static final Controller controller = getController();
    @Getter
    private final Floor floor;


    public FloorService(Floor floor) {
        this.floor = floor;
    }

    @Override
    public void run() {
        Person person;

        while (true) {
            if (!floor.isEmptyQueueUp()) {
                controller.addCallUp(floor.getFloorNumber());
            }
            if (!floor.isEmptyQueueDown()) {
                controller.addCallDown(floor.getFloorNumber());
            }

            if (floor.getQueueUp().size() + floor.getQueueDown().size() < QUEUE_SIZE) {
                person = generateAnyPerson(floor.getMaxFloor());


                if (person.isGoUp(floor.getFloorNumber())) {
                    //TODO: remove
                    person.setName("person from " + currentThread().getName() + " go to " + person.getTotalFloor());
                    System.out.println(person.getName());
                    //
                    floor.addToQueueUp(person);
                    controller.addCallUp(floor.getFloorNumber());
                } else {
                    if (person.isGoDown(floor.getFloorNumber())) {
                        //TODO: remove
                        person.setName("person from " + currentThread().getName() + " go to " + person.getTotalFloor());
                        System.out.println(person.getName());
                        //
                        floor.addToQueueDown(person);
                        controller.addCallDown(floor.getFloorNumber());
                    }
                }
            }
            floor.waitNewPerson();
        }
    }
}
