import java.util.Objects;
import java.util.concurrent.Semaphore;

public class Junction implements Runnable {
    private int greenTime;
    private Road[] entryRoads;
    private Road[] exitRoads;
    private Clock clock;
    private long currentTime;
    private int currentRoad;
    private long startTime;
    private Vehicle vehicle;
    private String[] destinationsReachable;
    private String exitRoadDestination;
//    private Semaphore junctionSemaphore;
    public Junction(int greenTime, Road[] entryRoads, Road[] exitRoads, Clock clock) {
        this.greenTime = (greenTime / 10);
        this.entryRoads = entryRoads;
        this.exitRoads = exitRoads;
        this.clock = clock;
        currentRoad = 0;
        startTime = clock.getCurrentTime();

//        this.junctionSemaphore = new Semaphore(1);
    }

    public void run() {
            try {
//                junctionSemaphore.acquire();
                while (true) {
                    if (currentRoad >= entryRoads.length){
                        currentRoad = 0;
                    }
//                    if (!entryRoads[currentRoad].isEmpty()) {
                        vehicle = entryRoads[currentRoad].removeVehicle();

                        String vehicleDestination = vehicle.getDestination();
                        outerLoop:
                        for (Road exitRoad : exitRoads) {
                            exitRoadDestination = exitRoad.getDestination();
                            if (exitRoadDestination.equals(vehicleDestination)) {
//                                if (!exitRoad.isFull()) {
                                    exitRoad.addVehicle(vehicle);

                                    Thread.sleep(10);

                                    currentTime = clock.getCurrentTime() - startTime;
                                    if (currentTime >= greenTime) {
                                        startTime = clock.getCurrentTime();
                                        currentRoad++;

                                    }
//                                    junctionSemaphore.release();
                                    break;
//                                }
                            }
                            destinationsReachable = exitRoad.getCouldBeReachedArray();
                            for (String s : destinationsReachable) {
                                if (s.equals(vehicleDestination)) {
//                                    if (!exitRoad.isFull()) {
                                        exitRoad.addVehicle(vehicle);
                                        Thread.sleep(10);
                                        currentTime = clock.getCurrentTime() - startTime;
                                        if (currentTime >= greenTime) {
                                            startTime = clock.getCurrentTime();
                                            currentRoad++;
                                        }
//                                        junctionSemaphore.release();
                                        break outerLoop;
//                                    }
                                }
                            }
                        }
//                    }
                }
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
            finally {
//                junctionSemaphore.release();
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