import java.util.concurrent.Semaphore;

public class Road {
    private Semaphore mutex = new Semaphore(1);
    //private Semaphore full = new Semaphore(0);
    private final int roadSize;
    //private Semaphore empty = new Semaphore(roadSize);
    public Road(int roadSize) {
        this.roadSize = roadSize;
        int [] roadQueue = new int[roadSize];
    }
    //method to check if space avail

    //method to add vehicle (produce)
    public void addVehicle() {
        for (int i = 0; i < roadSize; i++) {

        }
    }

    //method to remove vehicle (consume)
    public void removeVehicle() {

    }

}

/*
* Run as a queue (Buffer Class)
* it needs the entry point and the exit point to implement
* Needs a method to for entry point and junction to check if it can add a car
* Another method for junction and car park to check for a car to remove from the road
* 
*/