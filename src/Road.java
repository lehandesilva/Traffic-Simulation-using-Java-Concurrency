import java.util.concurrent.Semaphore;

public class Road {
    private Semaphore roadSemaphore;
    public Road() {
        
    }
}

/*
* Run as a queue (Buffer Class)
* it needs the entry point and the exit point to implement
* Needs a method to for entry point and junction to check if it can add a car
* Another method for junction and car park to check for a car to remove from the road
* 
*/