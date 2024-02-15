import java.util.concurrent.Semaphore;

public class Road {
    private final Semaphore mutex;
    private final int roadSize;
    private final String entryPoint;
    private final String destination;
    private final Vehicle[] cars;
    private final String[] couldBeReachedArray;
    private int frontPointer;
    private int rearPointer;
    private int count;

    public Road(int roadSize, String entry,String destination, String[] couldBeReachedArray) {
        this.mutex = new Semaphore(1);
        this.roadSize = roadSize;
        this.cars = new Vehicle[roadSize];
        this.entryPoint = entry;
        this.destination = destination;
        this.frontPointer = -1;
        this.rearPointer = -1;
        this.couldBeReachedArray = couldBeReachedArray;
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

    public void acquireMutex() throws InterruptedException {
        mutex.acquire();
    }

    public void releaseMutex() {
        mutex.release();
    }

    //method to add vehicle (produce)
    public void addVehicle(Vehicle car) throws InterruptedException {
//        mutex.acquire();
        if (frontPointer == -1) {
            frontPointer = 0;
        }
        rearPointer = (rearPointer + 1) % roadSize;
        cars[rearPointer] = car;
        count++;
//        mutex.release();
    }
    public Vehicle removeVehicle() throws InterruptedException{
//        mutex.acquire();
        Vehicle removedCar;
        if (frontPointer != -1) {
            removedCar = cars[frontPointer];
            count--;
            if (frontPointer == rearPointer) {
                frontPointer = -1;
                rearPointer = -1;
            } else {
                frontPointer = (frontPointer + 1) % roadSize;
            }
        }
        else {
            removedCar = null;
        }
//        mutex.release();
        return removedCar;
    }

    public String carDestination() {
        return cars[frontPointer].getDestination();
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