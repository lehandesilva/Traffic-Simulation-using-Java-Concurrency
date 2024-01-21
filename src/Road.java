import java.util.concurrent.Semaphore;

public class Road {
    private Semaphore mutex;
    //private Semaphore full = new Semaphore(0);
    private final int roadSize;
    private String entryPoint;
    private String destination;
    private Vehicle[] cars;
    private int frontPointer;
    private int rearPointer;
    private int count;

    public Road(int roadSize, String entry,String destination) {
        this.mutex = new Semaphore(1);
        this.roadSize = roadSize;
        this.cars = new Vehicle[roadSize];
        this.entryPoint = entry;
        this.destination = destination;
        this.frontPointer = -1;
        this.rearPointer = -1;
        this.count = 0;

    }
    //method to check if space avail
    public boolean isFull() {
        if (frontPointer ==0 && rearPointer == roadSize -1){
            return true;
        } else if (frontPointer == rearPointer + 1) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isEmpty() {
        if (frontPointer == -1) {
            return true;
        }
        else {
            return false;
        }
    }
    //method to add vehicle (produce)
    public boolean addVehicle(Vehicle car) throws InterruptedException {
        mutex.acquire();
        if (frontPointer == -1) {
            frontPointer = 0;
        }
        rearPointer = (rearPointer + 1) % roadSize;
        cars[rearPointer] = car;
        count++;
        mutex.release();
        return true;
        /*
        * Check if there is space available
        * add the car to the array
        * release the mutex*/
    }

    //method to remove vehicle (consume)
    public Vehicle removeVehicle() throws InterruptedException{
        mutex.acquire();
        Vehicle removedCar;
        removedCar = cars[frontPointer];
        if (frontPointer == rearPointer) {
            frontPointer = -1;
            rearPointer = -1;
        } else {
            frontPointer = (frontPointer + 1) % roadSize;
        }
        count--;
        mutex.release();
        return removedCar;
    }

}

/*
* Run as a queue (Buffer Class)
* it needs the entry point and the exit point to implement
* Needs a method to for entry point and junction to check if it can add a car
* Another method for junction and car park to check for a car to remove from the road
* 
*/