package task;

import task.entities.building.Building;
import task.service.StatisticService;

import static task.entities.building.Building.getBuilding;

public class Runner {
    public static void main(String[] args) {
        new Thread(new StatisticService()).start();
        Building building = getBuilding();
        building.startWork();
    }
}