import javax.print.attribute.standard.Destination;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class Junction implements Runnable{
    private final int greenTime;
    private final Road[] entryRoads;
    private final Road[] exitRoads;
    private final Clock clock;
    private int currentRoad;
    private long startTime;
    public Junction(int greenTime, Road[] entryRoads, Road[] exitRoads, Clock clock) {
        this.greenTime = (greenTime / 10);
        this.entryRoads = entryRoads;
        this.exitRoads = exitRoads;
        this.clock = clock;
        currentRoad = 0;
        startTime = clock.getCurrentTime();
    }
    /*
    * Loop for 360 seconds
    * get the entry road that's attached and check cars destination, get destination to it,
    * loop until there's a spot
    * grab mutex of both entry and exit, add and remove then release both */

    /*First approach -
     * Gets destination of current entry road
     * Attempt to grab mutex of both roads
     * Loop until there's a spot
     * Once there's a spot remove and add the car */

    /*Second Approach
     * Grab mutex of entry road
     * remove car
     * Figure out the exit road
     * hold the car in a loop until there's space in the exit road
     * Once there's a spot grab mutex and add car to the road
     * Release mutex */
    public void run() {
        try {
            while (clock.getCurrentTime() < 360) {
                if (currentRoad >= entryRoads.length) {
                    currentRoad = 0;
                }
                if (!entryRoads[currentRoad].isEmpty()) {
                    String vehicleDestination = entryRoads[currentRoad].carDestination();
                    Road exitRoad = findExitRoad(vehicleDestination);
                    if (exitRoad != null && !exitRoad.isFull()) {
                        try {
                            entryRoads[currentRoad].acquireMutex();
                            exitRoad.acquireMutex();
                            Vehicle vehicle = entryRoads[currentRoad].removeVehicle();
                            exitRoad.addVehicle(vehicle);
                            System.out.println("car crossed junction at : " + exitRoad.getEntryPoint());
                            Thread.sleep(100);
                        } finally {
                            exitRoad.releaseMutex();
                            entryRoads[currentRoad].releaseMutex();
                        }
                    }
                }
                long currentTime = clock.getCurrentTime() - startTime;
                if (currentTime >= greenTime) {
                    currentRoad++;
                    startTime = clock.getCurrentTime();
                    System.out.println("Changed light");
                }
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public Road findExitRoad(String vehicleDestination) {
        for (int i = 0; i < exitRoads.length; i++) {
            String exitRoadDestination = exitRoads[i].getDestination();
            if (exitRoadDestination.equals(vehicleDestination)) {
                return exitRoads[i];
            }
            else {
                for (int x = 0; x < exitRoads[i].getCouldBeReachedArray().length; x++) {
                    if (exitRoads[i].getCouldBeReachedArray()[x].equals(vehicleDestination)) {
                        return exitRoads[i];
                    }
                }
            }
        }
        System.out.println("Error: Couldn't find exit road");
        return null;
    }
}