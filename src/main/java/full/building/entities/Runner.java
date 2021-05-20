package full.building.entities;

import full.building.entities.building.Building;

import static full.building.entities.building.Building.getBuilding;

public class Runner {
    public static void main(String[] args) {
        Building building = getBuilding();
        building.startWork();
    }
}
