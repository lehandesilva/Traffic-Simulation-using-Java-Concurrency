import java.util.concurrent.Semaphore;

public class Road {
    private Semaphore mutex; // Binary semaphore for mutual exclusion
    private int nextIn; // pointer to the index that adds the next value
    private int nextOut; // pointer to the index that removes the next value
    private final String entryPoint;
    private final String destination;
    private final Vehicle[] cars; // Array holding cars
    private final String[] couldBeReachedArray;
    // Array that includes all the destinations that reachable through
    // this instance of the road object but not directly
    private boolean hasVehicles; // Flag if it has cars in array
    private int count;
    public Road(int roadSize, String entry,String destination, String[] couldBeReachedArray) {
        this.mutex = new Semaphore(1);
        this.cars = new Vehicle[roadSize];
        this.entryPoint = entry;
        this.destination = destination;
        this.couldBeReachedArray = couldBeReachedArray;
        this.count = 0;
        this.nextOut = 0;
        this.nextIn = 0;
        this.hasVehicles = false;

    }
    // method for threads to check if road is full before adding cars to it
    // Synchronized so doesn't read incorrect value
    synchronized public boolean isFull() {
        return count >= cars.length;
    }

    // method for threads to check if road has a vehicle before removing cars
    // Synchronized so doesn't read incorrect value and hold on to mutex when no cars are available
    synchronized public boolean hasVehicle() {
        return hasVehicles;
    }

    // method to add vehicle to array
    // if isFull returns true, cannot call method
    // Doesn't return anything
    public void addVehicle(Vehicle car) {
        try {
            // acquires mutex so calling thread will exclusive access to this object and adds car to
            // next available spot
            mutex.acquire();
            cars[nextIn] = car;
            // If pointer goes beyond length of array, value set back to zero and sets value of
            // hasVehicles to true so threads can call
            nextIn = (nextIn + 1) % cars.length;
            count++;
            hasVehicles = true;
        } catch (InterruptedException e) {
        }
        finally {
            mutex.release();
        }
    }
    // method to remove vehicle from array
    // can be called only if hasVehicles is true
    // returns an object of Vehicle class
    public Vehicle removeVehicle() {
        Vehicle car = null;
        try {
            // acquires mutex so calling thread will exclusive access to this object and removes car from
            // next out is pointing to
            mutex.acquire();
            car = cars[nextOut];
            nextOut = (nextOut + 1) % cars.length;
            count--;
            // If count is greater than zero, hasVehicles will be true
            hasVehicles = count > 0;
        } catch (InterruptedException e) {
        }
        finally {
            // Releases mutex at the end
            mutex.release();
        }
        if (car == null){
            throw new RuntimeException("Removing null car");
        }
        return car;
    }
    // Returns Destination of the car that next out is pointing at
    public String carDestination() {
        if (cars[nextOut] != null){
            return cars[nextOut].getDestination();
        }
        else {
            return null;
        }
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
