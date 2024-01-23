import java.util.Objects;

public class Junction implements Runnable {
    private int greenTime;
    private Road[] entryRoads;
    private Road[] exitRoads;
    private Clock clock;
    public Junction(Road[] entryRoads,Road[] exitRoads, Clock clock) {
        this.entryRoads = entryRoads;
        this.exitRoads = exitRoads;
        this.clock = clock;
    }

    public void run() {
        try {
            while (true) {
                for (int i = 0; i <=  entryRoads.length; i++) {
                    if (!entryRoads[i].isEmpty()) {
                        Vehicle vehicle = entryRoads[i].removeVehicle();
                        String vehicleDestination = vehicle.getDestination();
                        for (int x = 0; x <= exitRoads.length; x++) {
                            String exitRoadDestination = exitRoads[x].getDestination();
                            if (exitRoadDestination.equals(vehicleDestination) && !exitRoads[x].isFull()) {
                                exitRoads[x].addVehicle(vehicle);
                                break;
                            }
                            String[] destinationsReachable = exitRoads[x].getCouldBeReachedArray();
                            for (int y = 0; y <= destinationsReachable.length; y++) {
                                if (destinationsReachable[y].equals(vehicleDestination)) {
                                    exitRoads[x].addVehicle(vehicle);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}

/*
* Check the cars destination and check if there's a road with
  that destination on it and if not check the list of
  destinations reachable in the array of destinations of
  each exit road.
* junction should remove the car and store the car then pass
  it to the add car function of another road that's connected
  to it.
*  */

/*
* 12 cars per minute through the junction
* Acts as both a producer and consumer
* Requires a fixed number of entry and exit (max of 4 each)
Should be referred by compass direction and enabled and disabled
* One entry road to any exit road and after a preset time, should
change the road that has mutex
* The green light time should be configurable
* More shit in the brief just read through it
*/

/*
* Receives an array of roads and the roads should say where they
  lead to and from with variables like front and back
* */