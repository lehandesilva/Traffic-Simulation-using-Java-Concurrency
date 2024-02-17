import javax.print.attribute.standard.Destination;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class Junction extends Thread{
    private final int greenTime;
    private final Road[] entryRoads;
    private final Road[] exitRoads;
    private final Clock clock;
    private int currentRoad;
    private long startTime;
    private int carsPassed;
    private PrintWriter logWriter;
    public Junction(int greenTime, Road[] entryRoads, Road[] exitRoads, Clock clock) {
        this.greenTime = (greenTime / 10);
        this.entryRoads = entryRoads;
        this.exitRoads = exitRoads;
        this.clock = clock;
        currentRoad = 0;
        startTime = clock.getCurrentTime();
        this.carsPassed = 0;

        try {
            logWriter = new PrintWriter(new FileWriter("junction_log.txt", true));
        } catch (IOException e){
            throw new RuntimeException("Failed to initialize log file", e);
        }
    }

    @Override
    public void run() {
        try {
            while (!clock.hasStopped()) {
                if (currentRoad > (entryRoads.length - 1) ) {
                    currentRoad = 0;
                }
                if (entryRoads[currentRoad].hasVehicle()) {
                    Road exitRoad = findExitRoad(entryRoads[currentRoad].carDestination());
                    if (entryRoads[currentRoad].hasVehicle() && !exitRoad.isFull()) {
                        exitRoad.addVehicle(entryRoads[currentRoad].removeVehicle());
                        carsPassed++;
                        sleep(500);
                    }
                }
                long currentTime = clock.getCurrentTime();
                if ((currentTime - startTime) >= greenTime) {
                    if (carsPassed == 0) {
                        log(carsPassed + " cars through from " + entryRoads[currentRoad].getEntryPoint()+ ", " + entryRoads[currentRoad].getCount()+ " cars waiting. GRIDLOCK");
                    }
                    else {
                        carsPassed = 0;
                        log(carsPassed + " cars through from " + entryRoads[currentRoad].getEntryPoint()+ ", " + entryRoads[currentRoad].getCount()+ " cars waiting.");
                    }
                    currentRoad++;
                    startTime = clock.getCurrentTime();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Junction" + entryRoads[currentRoad].getEntryPoint()+ "Interrupted");
        }
    }

    private void log(String message) {
        logWriter.println("Time: " + clock.getCurrentTime() + "m - " + entryRoads[0].getDestination() + ": " + message);
        logWriter.flush();
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