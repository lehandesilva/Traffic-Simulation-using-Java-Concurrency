import java.util.concurrent.Semaphore;

public class Road {
    private Semaphore mutex;
    private final int roadSize;
    private String entryPoint;
    private String destination;
    private Vehicle[] cars;
    private String[] couldBeReachedArray;
    private int frontPointer;
    private int rearPointer;
    private final Semaphore emptySlots;
    private final Semaphore occupiedSlots;



    public Road(int roadSize, String entry,String destination, String[] couldBeReachedArray) {
        this.mutex = new Semaphore(1);
        this.roadSize = roadSize;
        this.cars = new Vehicle[roadSize];
        this.entryPoint = entry;
        this.destination = destination;
        this.frontPointer = -1;
        this.rearPointer = -1;
        this.couldBeReachedArray = couldBeReachedArray;
        this.emptySlots = new Semaphore(roadSize);
        this.occupiedSlots = new Semaphore(0);
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
    public void addVehicle(Vehicle car) throws InterruptedException {
        emptySlots.acquire();
        System.out.println("Thread " + Thread.currentThread().getName()+ "acquired empty");
        mutex.acquire();
        System.out.println("Thread " + Thread.currentThread().getName()+ "acquired mutex");
        if (frontPointer == -1) {
            frontPointer = 0;
        }
        rearPointer = (rearPointer + 1) % roadSize;
        cars[rearPointer] = car;
        mutex.release();
        System.out.println("Thread " + Thread.currentThread().getName()+ "released mutex");
        occupiedSlots.release();
        System.out.println("Thread " + Thread.currentThread().getName()+ "released empty");
        /*
        * Check if there is space available
        * add the car to the array
        * release the mutex*/
    }


    //method to remove vehicle (consume)
    public Vehicle removeVehicle() throws InterruptedException{
        occupiedSlots.acquire();
        System.out.println("Thread " + Thread.currentThread().getName()+ "acquired occupied");
        mutex.acquire();
        System.out.println("Thread " + Thread.currentThread().getName()+ "acquired mutex");
        Vehicle removedCar;
        removedCar = cars[frontPointer];
        if (frontPointer == rearPointer) {
            frontPointer = -1;
            rearPointer = -1;
        } else {
            frontPointer = (frontPointer + 1) % roadSize;
        }
        mutex.release();
        System.out.println("Thread " + Thread.currentThread().getName()+ "released mutex");
        emptySlots.release();
        System.out.println("Thread " + Thread.currentThread().getName()+ "released empty");
        return removedCar;
    }

    public String getDestination() {
        return destination;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public String[] getCouldBeReachedArray() {
        return couldBeReachedArray;
    }


}

/*
* Run as a queue (Buffer Class)
* it needs the entry point and the exit point to implement
* Needs a method to for entry point and junction to check if it can add a car
* Another method for junction and car park to check for a car to remove from the road
* 
*/