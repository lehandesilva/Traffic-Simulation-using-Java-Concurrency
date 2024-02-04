import javax.print.attribute.standard.Destination;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class Junction implements Runnable {
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
                Vehicle vehicle = entryRoads[currentRoad].removeVehicle();
                if (vehicle != null) {
                    //System.out.println("Vehicle added at junction " + entryRoads[currentRoad].getDestination());
                    String vehicleDestination = vehicle.getDestination();
                    Road exitRoad = findExitRoad(vehicleDestination);
                    if (exitRoad != null) {
                        if (!exitRoad.isFull()) {
                            exitRoad.addVehicle(vehicle);
                            //System.out.println("Car removed at junction " + exitRoad.getEntryPoint());
                            Thread.sleep(100);
                        } else {
                            while (exitRoad.isFull()) {
                                if (!exitRoad.isFull()) {
                                    exitRoad.addVehicle(vehicle);
                                    //      System.out.println("Car removed at junction " + exitRoad.getEntryPoint());
                                    Thread.sleep(100);
                                }
                            }
                        }
                    } else {
                        System.out.println("Null Road at " + entryRoads[0].getEntryPoint());
                    }
                }
                if (entryRoads[currentRoad].isFull()) {
                    System.out.println("Cock at " + entryRoads[currentRoad].getDestination());
                }

                long currentTime = clock.getCurrentTime() - startTime;
                if (currentTime >= greenTime) {
                    currentRoad++;
                    startTime = clock.getCurrentTime();
                    System.out.println("Changed Lane");
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
        System.out.println("Returning null");
        return null;
    }
}