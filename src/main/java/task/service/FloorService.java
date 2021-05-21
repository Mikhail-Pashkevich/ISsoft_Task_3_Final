package task.service;

import task.entities.controller.Controller;
import task.entities.floor.Floor;
import task.entities.person.Person;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static task.entities.building.Building.generatePerson;
import static task.entities.building.BuildingConstant.MAX_FLOOR;
import static task.entities.controller.Controller.getController;
import static task.entities.floor.FloorConstant.QUEUE_SIZE;
import static java.lang.Thread.currentThread;

@Slf4j
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
                person = generatePerson(MAX_FLOOR);

                if (person.isGoUp(floor.getFloorNumber())) {
                    person.setName("person from " + currentThread().getName() + " go to floor " + person.getTotalFloor());
                    log.info(currentThread().getName() + ": " + person.getName());

                    floor.addToQueueUp(person);
                    controller.addCallUp(floor.getFloorNumber());
                } else {
                    if (person.isGoDown(floor.getFloorNumber())) {
                        person.setName("person from " + currentThread().getName() + " go to floor " + person.getTotalFloor());
                        log.info(currentThread().getName() + ": " + person.getName());

                        floor.addToQueueDown(person);
                        controller.addCallDown(floor.getFloorNumber());
                    }
                }
            }
            floor.waitNewPerson();
        }
    }
}