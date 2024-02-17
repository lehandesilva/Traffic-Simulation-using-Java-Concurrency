import java.util.concurrent.Semaphore;

public class Road {
    private Semaphore mutex;
    private int nextIn;
    private int nextOut;
    private final int roadSize;
    private final String entryPoint;
    private final String destination;
    private final Vehicle[] cars;
    private final String[] couldBeReachedArray;
    private boolean hasVehicles;
    private int count;
    public Road(int roadSize, String entry,String destination, String[] couldBeReachedArray) {
        this.mutex = new Semaphore(1);
        this.roadSize = roadSize;
        this.cars = new Vehicle[roadSize];
        this.entryPoint = entry;
        this.destination = destination;
        this.couldBeReachedArray = couldBeReachedArray;
        this.count = 0;
        this.nextOut = 0;
        this.nextIn = 0;
        this.hasVehicles = false;

    }
    //method to check if space avail
    synchronized public boolean isFull() {
        return count >= cars.length;
    }

    synchronized public boolean hasVehicle() {
        return hasVehicles;
    }
    public void addVehicle(Vehicle car) {
        try {
            mutex.acquire();
            cars[nextIn] = car;
            nextIn = (nextIn + 1) % cars.length;
            count++;
            hasVehicles = true;
        } catch (InterruptedException e) {
        }
        finally {
            mutex.release();
        }
    }
    public Vehicle removeVehicle() {
        Vehicle car = null;
        try {
            mutex.acquire();
            car = cars[nextOut];
            nextOut = (nextOut + 1) % cars.length;
            count--;
            hasVehicles = count > 0;
        } catch (InterruptedException e) {
        }
        finally {
            mutex.release();
        }
        if (car == null){
            throw new RuntimeException("Removing null car");
        }
        return car;
    }
    public String carDestination() {
        if (cars[nextOut] != null){
            return cars[nextOut].getDestination();
        }
        else {
            return null;
        }
    }

    public int getRoadSize() {
        return roadSize;
    }

    public int getCount() {
        return count;
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
