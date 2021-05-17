package entities;

import static constants.Const.MAX_FLOOR;
import static constants.Const.MAX_LIFT;
import static java.lang.Thread.currentThread;

public class Runner {
    public static void main(String[] args) {
        Building building =Building.getBuilding(MAX_FLOOR,MAX_LIFT);
        building.startWork();
//        try {
//            System.out.println("here");
//            currentThread().wait();
//            System.out.println("here");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
