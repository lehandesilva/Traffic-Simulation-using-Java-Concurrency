import javax.print.attribute.standard.Destination;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class Junction extends Thread{
    private final int greenTime; // time that each road will stay green for allowing cars to move
    private final Road[] entryRoads; // entry roads to the junction
    private final Road[] exitRoads; // exit roads of the junction
    private final Clock clock;
    private int currentRoad; // pointer to current road its looping on
    private long startTime; // to hold the time that the green light came on
    private int carsPassed; // cars passed through junction
    private final PrintWriter logWriter;
    public Junction(int greenTime, Road[] entryRoads, Road[] exitRoads, Clock clock) {
        this.greenTime = (greenTime / 10); // to turn simulation time to real time
        this.entryRoads = entryRoads;
        this.exitRoads = exitRoads;
        this.clock = clock;
        currentRoad = 0;
        startTime = clock.getCurrentTime();
        this.carsPassed = 0;

        // Logs messages every time the light changes
        try {
            logWriter = new PrintWriter(new FileWriter("junction_log.txt", true));
        } catch (IOException e){
            throw new RuntimeException("Failed to initialize log file", e);
        }
    }

    @Override
    public void run() {
        try {
            // Runs until clock.hasStopped is true
            while (!clock.hasStopped()) {
                // checks if currentroad has exceeded length of the entry roads available
                // and if it has, sets it back to 0, used to point to entry roads array
                if (currentRoad > (entryRoads.length - 1) ) {
                    currentRoad = 0;
                }
                // Checks if entry road has a vehicle before modifying any road, gets the destination
                // of that available car to find the exit road for vehicle
                if (entryRoads[currentRoad].hasVehicle()) {
                    Road exitRoad = findExitRoad(entryRoads[currentRoad].carDestination());
                    // Checks if that exit road has space and then removes vehicle from the entry road and immediately
                    // adds it to the designated exit road so car is lost between transfer or if green light changes in between
                    // sleeps for half a second to simulate car passing through junction
                    if (entryRoads[currentRoad].hasVehicle() && !exitRoad.isFull()) {
                        exitRoad.addVehicle(entryRoads[currentRoad].removeVehicle());
                        carsPassed++;
                        sleep(500);
                    }
                }
                long currentTime = clock.getCurrentTime();
                // checks if time on road has exceeded green time and if so, logs details, changes road and start time to current time
                if ((currentTime - startTime) >= greenTime) {
                    if (carsPassed == 0) {
                        log(carsPassed + " cars through from " + entryRoads[currentRoad].getEntryPoint()+ ", " + entryRoads[currentRoad].getCount()+ " cars waiting. GRIDLOCK");
                    }
                    else {
                        log(carsPassed + " cars through from " + entryRoads[currentRoad].getEntryPoint()+ ", " + entryRoads[currentRoad].getCount()+ " cars waiting.");
                        carsPassed = 0;
                    }
                    currentRoad++;
                    startTime = clock.getCurrentTime();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Junction" + entryRoads[currentRoad].getEntryPoint()+ "Interrupted");
        }
    }

    // method to log messages
    private void log(String message) {
        logWriter.println("Time: " + clock.getCurrentTime() + "m - " + entryRoads[0].getDestination() + ": " + message);
        logWriter.flush();
    }

    // Method to find exit road current car
    // checks each exit road's immediate destination and then checks its
    // destinations are reachable through other roads connected to it
    // Loops through all exit roads until road is found
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