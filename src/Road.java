import java.util.concurrent.Semaphore;

public class Road {
    private Semaphore mutex;
    //private Semaphore full = new Semaphore(0);
    private final int roadSize;
    //private Semaphore empty = new Semaphore(roadSize);
    private string entryPoint;
    private string destination;
    private Vehicle[] cars;

    public Road(int roadSize, string entry,string destination) {
        this.mutex = new Semaphore(1);
        this.roadSize = roadSize;
        this.cars = new Vehicle[roadSize];
        this.entryPoint = entry;
        this.destination = destination;
    }
    //method to check if space avail

    //method to add vehicle (produce)
    public void addVehicle(Vehicle car) throws InterruptedException {
        mutex.acquire();
        /*
        * Check if there is space available
        * add the car to the array
        * release the mutex*/

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