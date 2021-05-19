package entities;

import entities.building.Building;

import static entities.building.Building.getBuilding;

public class Runner {
    public static void main(String[] args) {
        Building building = getBuilding();
        building.startWork();
    }
}
